package neil.com.daggerdemo.ui.news.contract;

import java.util.List;

import neil.com.daggerdemo.base.BaseContract;
import neil.com.daggerdemo.bean.NewsDetail;

/**
 * 频道新闻详情
 * Created by neil on 2018/3/2 0002.
 */

public interface DetailContract {

    interface View extends BaseContract.BaseView {

        /**
         * 加载顶部banner 数据
         *
         * @param newsDetail
         */
        void loadBannerData(NewsDetail newsDetail);

        /**
         * 加载置顶新闻数据
         *
         * @param newsDetail
         */
        void loadTopNewsData(NewsDetail newsDetail);

        /**
         * 加载新闻数据
         *
         * @param itemBeanList
         */
        void loadData(List<NewsDetail.ItemBean> itemBeanList);

        /**
         * 加载更多新闻数据
         *
         * @param itemBeanList
         */
        void loadMoreData(List<NewsDetail.ItemBean> itemBeanList);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        /**
         * 获取新闻详细信息
         *
         * @param id      频道ID值
         * @param action  用户操作方式
         *                1.下拉 down
         *                2.上拉 up
         *                3.默认 default
         * @param pullNum 操作次数 累加
         */
        void getData(String id, String action, int pullNum);
    }

}
