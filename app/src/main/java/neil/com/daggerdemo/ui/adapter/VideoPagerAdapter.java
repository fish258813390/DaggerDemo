package neil.com.daggerdemo.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import neil.com.daggerdemo.base.LazyBaseFragment;
import neil.com.daggerdemo.bean.VideoChannelBean;
import neil.com.daggerdemo.ui.video.VideoDetailFragment;

/**
 * 视频频道viewpager 适配器
 *
 * @author neil
 * @date 2018/3/9
 */
public class VideoPagerAdapter extends FragmentStatePagerAdapter {

    private VideoChannelBean videoChannelBean;

    public VideoPagerAdapter(FragmentManager fm, VideoChannelBean videoChannelBean) {
        super(fm);
        this.videoChannelBean = videoChannelBean;
    }

    @Override
    public LazyBaseFragment getItem(int position) {
        return VideoDetailFragment.newInstance("clientvideo_" + videoChannelBean.getTypes().get(position).getId());
    }

    @Override
    public int getCount() {
        return videoChannelBean != null ? videoChannelBean.getTypes().size() : 0;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return videoChannelBean.getTypes().get(position).getName();
    }
}
