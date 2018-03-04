package neil.com.daggerdemo.ui.news.contract;

import java.util.List;

import neil.com.daggerdemo.base.BaseContract;
import neil.com.daggerdemo.bean.Channel;

/**
 * Created by neil on 2018/3/2 0002.
 */

public interface NewsContract {

    interface View extends BaseContract.BaseView {

        void loadData(List<Channel> channels, List<Channel> otherChannels);

    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        /**
         * 初始化频道
         */
        void getChannel();

    }
}
