package neil.com.daggerdemo.ui.news;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import neil.com.daggerdemo.R;
import neil.com.daggerdemo.base.BaseFragment;
import neil.com.daggerdemo.bean.Channel;
import neil.com.daggerdemo.di.component.DaggerFragmentComponent;
import neil.com.daggerdemo.ui.adapter.ChannelPagerAdapter;
import neil.com.daggerdemo.ui.news.contract.NewsContract;
import neil.com.daggerdemo.ui.news.presenter.NewsPresenter;
import neil.com.daggerdemo.widget.CustomViewPager;

/**
 * @author neil
 * @date 2018/3/2
 */

public class NewsFragment extends BaseFragment<NewsPresenter> implements NewsContract.View{

    private static final String TAG = "NewsFragment";

    @BindView(R.id.viewpager)
    CustomViewPager mViewpager;
    @BindView(R.id.iv_edit)
    ImageView mIvEdit;
    @BindView(R.id.SlidingTabLayout)
    com.flyco.tablayout.SlidingTabLayout SlidingTabLayout;

    private ChannelPagerAdapter mChannelPagerAdapter;
    private List<Channel> mSelectedDatas;
    private List<Channel> mUnSelectedDatas;

    private int selectedIndex;
    private String selectedChannel;

    public static NewsFragment newInstance(){
        Bundle args = new Bundle();
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news_new;
    }

    @Override
    protected void initView(View view) {
        mSelectedDatas = new ArrayList<>();
        mUnSelectedDatas = new ArrayList<>();
        mPresenter.getChannel();

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedIndex = position;
                selectedChannel = mSelectedDatas.get(position).getChannelName();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void loadData(List<Channel> channels, List<Channel> unSelectedDatas) {
        if (channels != null) {
            mSelectedDatas.clear();
            mSelectedDatas.addAll(channels);
            mUnSelectedDatas.clear();
            mUnSelectedDatas.addAll(unSelectedDatas);
            mChannelPagerAdapter = new ChannelPagerAdapter(getChildFragmentManager(), channels);
            mViewpager.setAdapter(mChannelPagerAdapter);
            mViewpager.setOffscreenPageLimit(2);
            mViewpager.setCurrentItem(0, false);
            SlidingTabLayout.setViewPager(mViewpager);
        } else {
            ToastUtils.showShort("数据异常");
        }
    }
}
