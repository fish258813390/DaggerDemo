package neil.com.daggerdemo.ui.video;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;

import java.util.List;

import butterknife.BindView;
import neil.com.daggerdemo.R;
import neil.com.daggerdemo.base.BaseFragment;
import neil.com.daggerdemo.base.LazyBaseFragment;
import neil.com.daggerdemo.bean.VideoChannelBean;
import neil.com.daggerdemo.bean.VideoDetailBean;
import neil.com.daggerdemo.ui.adapter.VideoPagerAdapter;
import neil.com.daggerdemo.ui.video.contract.VideoContract;
import neil.com.daggerdemo.ui.video.presenter.VideoPresenter;

/**
 * 视频fragment
 *
 * @author neil
 * @date 2018/3/9
 */

public class VideoFragment extends BaseFragment<VideoPresenter> implements VideoContract.View {

    private static final String TAG = "VideoFragment";
    @BindView(R.id.tablayout)
    TabLayout mTablayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    private VideoPagerAdapter mVideoPagerAdapter;

    public static VideoFragment newInstance() {
        Bundle args = new Bundle();
        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initView(View view) {
        mPresenter.getViewChannel();
    }

    @Override
    public void onRetry() {

    }

    @Override
    public void loadVideoChannel(List<VideoChannelBean> channelBeanList) {
        LogUtils.i(TAG, "loadVideoChannel: " + channelBeanList.toString());
        mVideoPagerAdapter = new VideoPagerAdapter(getChildFragmentManager(), channelBeanList.get(0));
        mViewpager.setAdapter(mVideoPagerAdapter);
        mViewpager.setOffscreenPageLimit(1);
        mViewpager.setCurrentItem(0, false);
        mTablayout.setupWithViewPager(mViewpager, true);
    }

    @Override
    public void loadVideoDetails(List<VideoDetailBean> detailBeanList) {

    }

    @Override
    public void loadMoreVideoDetails(List<VideoDetailBean> detailBeanList) {

    }


}
