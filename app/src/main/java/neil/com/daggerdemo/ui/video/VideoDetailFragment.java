package neil.com.daggerdemo.ui.video;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import neil.com.daggerdemo.R;
import neil.com.daggerdemo.base.LazyBaseFragment;
import neil.com.daggerdemo.bean.VideoChannelBean;
import neil.com.daggerdemo.bean.VideoDetailBean;
import neil.com.daggerdemo.ui.adapter.VideoDetailAdapter;
import neil.com.daggerdemo.ui.video.contract.VideoContract;
import neil.com.daggerdemo.ui.video.presenter.VideoPresenter;
import neil.com.daggerdemo.widget.CustomLoadMoreView;

/**
 * 视频
 *
 * @author neil
 * @date 2018/3/9
 */

public class VideoDetailFragment extends LazyBaseFragment<VideoPresenter> implements VideoContract.View {

    public static final String TYPEID = "typeId";

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mPtrFrameLayout)
    PtrClassicFrameLayout mPtrFrameLayout;
    private VideoDetailBean videoDetailBean;
    private VideoDetailAdapter detailAdapter;
    private int pageNum = 1;
    private String typeId;

    public static VideoDetailFragment newInstance(String typeId) {
        Bundle args = new Bundle();
        args.putString(TYPEID, typeId);
        VideoDetailFragment fragment = new VideoDetailFragment();
        fragment.setArguments(args);
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
        mPtrFrameLayout.disableWhenHorizontalMove(true);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageNum = 1;
                if (mPresenter != null) {
                    mPresenter.getVideoDetails(pageNum, "list", typeId);
                }
            }
        });

        videoDetailBean = new VideoDetailBean();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(detailAdapter = new VideoDetailAdapter(getActivity(),
                R.layout.item_detail_video, videoDetailBean.getItem()));
        detailAdapter.setEnableLoadMore(true);
        detailAdapter.setLoadMoreView(new CustomLoadMoreView());
        detailAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        detailAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getVideoDetails(pageNum, "list", typeId);
            }
        }, mRecyclerView);

    }

    @Override
    protected void initData() {
        if (getArguments() == null) {
            return;
        }
        typeId = getArguments().getString(TYPEID);
        if (mPresenter != null) {
            mPresenter.getVideoDetails(pageNum, "list", typeId);
        }
    }

    @Override
    public void loadVideoDetails(List<VideoDetailBean> detailBeanList) {
        if(detailBeanList == null){
            showFaild("");
            return;
        }
        pageNum ++;
        detailAdapter.setNewData(detailBeanList.get(0).getItem());
        mPtrFrameLayout.refreshComplete();
        showSuccess("");
    }

    @Override
    public void loadMoreVideoDetails(List<VideoDetailBean> detailBeanList) {
        if(detailBeanList == null){
            detailAdapter.loadMoreEnd();
            return;
        }
        pageNum ++;
        detailAdapter.addData(detailBeanList.get(0).getItem());
        detailAdapter.loadMoreComplete();
    }

    @Override
    public void loadVideoChannel(List<VideoChannelBean> channelBeanList) {

    }

    @Override
    public void onRetry() {
        initData();
    }
}
