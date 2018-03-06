package neil.com.daggerdemo.ui.news;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import neil.com.daggerdemo.R;
import neil.com.daggerdemo.base.BaseFragment;
import neil.com.daggerdemo.base.LazyBaseFragment;
import neil.com.daggerdemo.bean.NewsDetail;
import neil.com.daggerdemo.ui.adapter.NewsDetailAdapter;
import neil.com.daggerdemo.ui.news.contract.DetailContract;
import neil.com.daggerdemo.ui.news.presenter.DetailPresenter;
import neil.com.daggerdemo.utils.GlideImageLoader;
import neil.com.daggerdemo.widget.CustomLoadMoreView;

/**
 * 新闻详情fragment
 *
 * @author neil
 * @date 2018/3/2
 */

public class DetailFragment extends LazyBaseFragment<DetailPresenter> implements DetailContract.View {
    private static final String TAG = "JdDetailFragment";

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mPtrFrameLayout)
    PtrFrameLayout mPtrFrameLayout;
    @BindView(R.id.tv_toast)
    TextView mTvToast;
    @BindView(R.id.rl_top_toast)
    RelativeLayout mRlTopToast; // 顶部消息提示

    private View view_Focus;// 顶部banner
    private Banner mBanner;
    private String newsid;
    private int position;
    private List<NewsDetail.ItemBean> beanList;
    private List<NewsDetail.ItemBean> mBannerList;
    private NewsDetailAdapter detailAdapter;
    private int upPullNum = 1;
    private int downPullNum = 1;
    private boolean isRemoveHeaderView = false;

    public static DetailFragment newInstance(String newsid, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("newsid", newsid);
        bundle.putInt("position", position);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail;
    }

    @Override
    protected void initView(View view) {
        //
        mPtrFrameLayout.disableWhenHorizontalMove(true);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                Log.i(TAG, "onRefreshBegin: " + downPullNum);
                isRemoveHeaderView = true;
                mPresenter.getData(newsid, "down", downPullNum);
            }
        });
        beanList = new ArrayList<>();
        mBannerList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(detailAdapter = new NewsDetailAdapter(beanList, getActivity()));
        detailAdapter.setEnableLoadMore(true);
        detailAdapter.setLoadMoreView(new CustomLoadMoreView());
        detailAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        detailAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                Log.i(TAG, "onLoadMoreRequested: " + upPullNum);
                mPresenter.getData(newsid, "up", upPullNum);

            }
        }, mRecyclerView);
        // item点击事件

        view_Focus = view.inflate(getActivity(), R.layout.news_detail_headerview, null);
        mBanner = (Banner) view_Focus.findViewById(R.id.banner);
        // 设置banner样式
        mBanner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        GlideImageLoader.loadImage(getActivity(), path, imageView);
                    }
                })
                .setDelayTime(3000)
                .setIndicatorGravity(BannerConfig.RIGHT);
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if(mBannerList.size() < 1){
                    return;
                }
                ToastUtils.showShort("banner--->" + position);
            }
        });
    }

    @Override
    protected void initData() {
        if (getArguments() == null) return;
        newsid = getArguments().getString("newsid");
        position = getArguments().getInt("position");
        mPresenter.getData(newsid, "default", 1);
    }

    @Override
    public void loadBannerData(NewsDetail newsDetail) {
        List<String> mTitleList = new ArrayList<>();
        List<String> mUrlList = new ArrayList<>();
        mBannerList.clear();
        for (NewsDetail.ItemBean bean : newsDetail.getItem()) {
            if (!TextUtils.isEmpty(bean.getThumbnail())) {
                mTitleList.add(bean.getTitle());
                mBannerList.add(bean);
                mUrlList.add(bean.getThumbnail());
            }
        }
        if (mUrlList.size() > 0) {
            mBanner.setImages(mUrlList);
            mBanner.setBannerTitles(mTitleList);
            mBanner.start();
            if (detailAdapter.getHeaderLayoutCount() < 1) {
                detailAdapter.addHeaderView(view_Focus);
            }
        }
    }

    @Override
    public void loadTopNewsData(NewsDetail newsDetail) {

    }

    @Override
    public void loadData(List<NewsDetail.ItemBean> itemBeanList) {
        if (itemBeanList == null || itemBeanList.size() == 0) {
            showFaild("");
            mPtrFrameLayout.refreshComplete();
        } else {
            downPullNum++;
            if (isRemoveHeaderView) {
                detailAdapter.removeAllHeaderView();
            }
            detailAdapter.setNewData(itemBeanList);
            showToast(itemBeanList.size(), true);
            mPtrFrameLayout.refreshComplete();
            showSuccess("");
            Log.i(TAG, "loadData: " + itemBeanList.toString());
        }
    }

    @Override
    public void loadMoreData(List<NewsDetail.ItemBean> itemBeanList) {

    }

    private void showToast(int num, boolean isRefresh) {
        if (isRefresh) {
            mTvToast.setText(String.format(getResources().getString(R.string.news_toast), num + ""));
        } else {
            mTvToast.setText("将为你减少此类内容");
        }
        mRlTopToast.setVisibility(View.VISIBLE);
//        ViewAnimator.animate(mRlTopToast)
//                .newsPaper()
//                .duration(1000)
//                .start()
//                .onStop(new Animation.AnimationListener.Stop() {
//                    @Override
//                    public void onStop() {
//                        ViewAnimator.animate(mRlTopToast)
//                                .bounceOut()
//                                .duration(1000)
//                                .start();
//                    }
//                });
    }
}
