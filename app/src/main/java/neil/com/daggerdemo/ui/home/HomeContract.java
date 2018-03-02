package neil.com.daggerdemo.ui.home;

import java.util.List;

import neil.com.daggerdemo.base.BaseContract;
import neil.com.daggerdemo.bean.Article;
import neil.com.daggerdemo.bean.Banner;
import neil.com.daggerdemo.constant.LoadType;

/**
 * 主页
 *
 * @author neil
 * @date 2018/3/2
 */

public interface HomeContract {

    interface View extends BaseContract.BaseView {

        void setHomeBanners(List<Banner> banners);

        void setHomeArticles(Article article, @LoadType.checker int loadType);

        void collectArticleSuccess(int position, Article.DatasBean bean);

    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void loadHomeBanners();

        void loadHomeArticles();

        void refresh();

        void loadMore();

        void collectArticle(int position, Article.DatasBean bean);

        void loadHomeData();

    }

}
