package neil.com.daggerdemo.ui.video.contract;

import java.util.List;

import neil.com.daggerdemo.base.BaseContract;
import neil.com.daggerdemo.bean.VideoChannelBean;
import neil.com.daggerdemo.bean.VideoDetailBean;

/**
 * 视频契约类
 *
 * @author neil
 * @date 2018/3/9
 */
public interface VideoContract {

    interface View extends BaseContract.BaseView {

        /**
         * 加载视频频道
         * @param channelBeanList
         */
        void loadVideoChannel(List<VideoChannelBean> channelBeanList);

        /**
         * 视频详情列表
         * @param detailBeanList
         */
        void loadVideoDetails(List<VideoDetailBean> detailBeanList);

        void loadMoreVideoDetails(List<VideoDetailBean> detailBeanList);

    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        /**
         * 获取视频频道列表
         */
        void getViewChannel();

        /**
         * 获取视频列表
         * @param page 页码
         * @param listType 默认list
         * @param typeId 频道id
         */
        void getVideoDetails(int page, String listType, String typeId);

    }


}
