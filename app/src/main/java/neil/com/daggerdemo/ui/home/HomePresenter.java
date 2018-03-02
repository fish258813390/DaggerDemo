package neil.com.daggerdemo.ui.home;

import com.blankj.utilcode.util.SPUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;
import neil.com.daggerdemo.R;
import neil.com.daggerdemo.base.App;
import neil.com.daggerdemo.base.BasePresenter;
import neil.com.daggerdemo.bean.Article;
import neil.com.daggerdemo.bean.Banner;
import neil.com.daggerdemo.bean.DataResponse;
import neil.com.daggerdemo.bean.User;
import neil.com.daggerdemo.constant.Constant;
import neil.com.daggerdemo.constant.LoadType;
import neil.com.daggerdemo.net.ApiService;
import neil.com.daggerdemo.net.RetrofitManager;
import neil.com.daggerdemo.utils.RxSchedulers;

/**
 * @author neil
 * @date 2018/3/2
 */

public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter {

    private int mPage;
    private boolean mIsRefresh;

    @Inject
    public HomePresenter() {
        this.mIsRefresh = true;
    }

    @Override
    public void loadHomeBanners() {
        RetrofitManager.create(ApiService.class)
                .getHomeBanners()
                .compose(RxSchedulers.<DataResponse<List<Banner>>>applySchedulers())
                .compose(mView.<DataResponse<List<Banner>>>bindToLife())
                .subscribe(new Consumer<DataResponse<List<Banner>>>() {
                    @Override
                    public void accept(DataResponse<List<Banner>> listDataResponse) throws Exception {
                        mView.setHomeBanners(listDataResponse.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showFaild(throwable.getMessage());
                    }
                });
    }

    @Override
    public void loadHomeArticles() {
        RetrofitManager.create(ApiService.class)
                .getHomeArticles(mPage)
                .compose(RxSchedulers.<DataResponse<Article>>applySchedulers())
                .compose(mView.<DataResponse<Article>>bindToLife())
                .subscribe(new Consumer<DataResponse<Article>>() {
                    @Override
                    public void accept(DataResponse<Article> articleDataResponse) throws Exception {
                        int loadType = mIsRefresh ? LoadType.TYPE_REFRESH_SUCCESS : LoadType.TYPE_LOAD_MORE_SUCCESS;
                        mView.setHomeArticles(articleDataResponse.getData(), loadType);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        int loadType = mIsRefresh ? LoadType.TYPE_REFRESH_ERROR : LoadType.TYPE_LOAD_MORE_ERROR;
                        mView.setHomeArticles(new Article(), loadType);
                    }
                });
    }

    @Override
    public void refresh() {
        mPage = 0;
        mIsRefresh = true;
        loadHomeBanners();
        loadHomeArticles();
    }

    @Override
    public void loadMore() {
        mPage++;
        mIsRefresh = false;
        loadHomeArticles();
    }

    @Override
    public void collectArticle(int position, Article.DatasBean bean) {

    }

    //
    @Override
    public void loadHomeData() {
        mView.showLoading();
        String username = SPUtils.getInstance(Constant.SHARED_NAME).getString(Constant.USERNAME_KEY);
        String password = SPUtils.getInstance(Constant.SHARED_NAME).getString(Constant.PASSWORD_KEY);
        Observable<DataResponse<User>> observableUser = RetrofitManager.create(ApiService.class).login(username, password);
        Observable<DataResponse<List<Banner>>> observableBanner = RetrofitManager.create(ApiService.class).getHomeBanners();
        Observable<DataResponse<Article>> observableArticle = RetrofitManager.create(ApiService.class).getHomeArticles(mPage);
        Observable.zip(observableUser,
                observableBanner,
                observableArticle
                , new Function3<DataResponse<User>,
                        DataResponse<List<Banner>>,
                        DataResponse<Article>,
                        Map<String, Object>>() {

                    @Override
                    public Map<String, Object> apply(DataResponse<User> response, DataResponse<List<Banner>> listDataResponse, DataResponse<Article> articleDataResponse) throws Exception {
                        Map<String, Object> objMap = new HashMap<>();
                        objMap.put(Constant.USER_KEY, response);
                        objMap.put(Constant.BANNER_KEY, listDataResponse);
                        objMap.put(Constant.ARTICLE_KEY, articleDataResponse);
                        return objMap;
                    }
                })
                .compose(RxSchedulers.<Map<String, Object>>applySchedulers())
                .compose(mView.<Map<String, Object>>bindToLife())
                .subscribe(new Consumer<Map<String, Object>>() {
                    @Override
                    public void accept(Map<String, Object> map) throws Exception {
                        DataResponse<User> dataResponse = (DataResponse<User>) map.get(Constant.USER_KEY);
                        if (dataResponse.getErrorCode() == 0) {
                            mView.showSuccess(
                                    App.getAppContext().getString(R.string.auto_login_success));
                        } else {
                            mView.showFaild(String.valueOf(dataResponse.getErrorMsg()));
                        }
                        DataResponse<List<Banner>> banners = (DataResponse<List<Banner>>) map.get(Constant.BANNER_KEY);
                        DataResponse<Article> article = (DataResponse<Article>) map.get(Constant.ARTICLE_KEY);
                        mView.setHomeBanners(banners.getData());
                        mView.setHomeArticles(article.getData(), LoadType.TYPE_REFRESH_SUCCESS);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showFaild(throwable.getMessage());
                    }
                });
    }
}
