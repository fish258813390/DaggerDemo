package neil.com.daggerdemo.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import neil.com.daggerdemo.base.BaseFragment;
import neil.com.daggerdemo.base.LazyBaseFragment;
import neil.com.daggerdemo.bean.Channel;
import neil.com.daggerdemo.ui.news.DetailFragment;
import neil.com.daggerdemo.ui.news.presenter.DetailPresenter;

/**
 * Created by neil on 2018/3/2 0002.
 */

public class ChannelPagerAdapter extends FragmentStatePagerAdapter {

    private List<Channel> mChannels;

    public ChannelPagerAdapter(FragmentManager fm, List<Channel> channels) {
        super(fm);
        this.mChannels = channels;
    }

    public void updateChannel(List<Channel> channels) {
        this.mChannels = channels;
        notifyDataSetChanged();
    }

    @Override
    public LazyBaseFragment getItem(int position) {
        return DetailFragment.newInstance(mChannels.get(position).getChannelId(), position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mChannels.get(position).getChannelName();
    }

    @Override
    public int getCount() {
        return mChannels != null ? mChannels.size() : 0;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
