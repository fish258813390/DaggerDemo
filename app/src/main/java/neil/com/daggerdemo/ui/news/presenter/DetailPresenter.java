package neil.com.daggerdemo.ui.news.presenter;

import org.litepal.util.LogUtil;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import neil.com.daggerdemo.base.BasePresenter;
import neil.com.daggerdemo.bean.NewsDetail;
import neil.com.daggerdemo.constant.Constant;
import neil.com.daggerdemo.net.ApiService;
import neil.com.daggerdemo.net.BaseObserver;
import neil.com.daggerdemo.net.NewsUtils;
import neil.com.daggerdemo.net.RetrofitManager;
import neil.com.daggerdemo.ui.news.contract.DetailContract;
import neil.com.daggerdemo.utils.RxSchedulers;

/**
 * Created by neil on 2018/3/3 0003.
 */

public class DetailPresenter extends BasePresenter<DetailContract.View> implements DetailContract.Presenter {

    private static final String TAG = "DetailPresenter";

    @Inject
    public DetailPresenter() {

    }

    @Override
    public void getData(String id, final String action, int pullNum) {
        RetrofitManager.create(ApiService.class, Constant.sIFengApi)
                .getNewsDetail(id, action, pullNum)
                .compose(RxSchedulers.<List<NewsDetail>>applySchedulers())
                .map(new Function<List<NewsDetail>, NewsDetail>() {

                    @Override
                    public NewsDetail apply(List<NewsDetail> newsDetails) throws Exception {
                        for (NewsDetail newsDetail : newsDetails) {
                            if (NewsUtils.isBannerNews(newsDetail)) {
                                mView.loadBannerData(newsDetail);
                            }
                            if (NewsUtils.isTopNews(newsDetail)) {
                                mView.loadTopNewsData(newsDetail);
                            }
                        }
                        return newsDetails.get(0);
                    }
                })
                .map(new Function<NewsDetail, List<NewsDetail.ItemBean>>() {

                    @Override
                    public List<NewsDetail.ItemBean> apply(NewsDetail newsDetail) throws Exception {
                        Iterator<NewsDetail.ItemBean> iterator = newsDetail.getItem().iterator();
                        while (iterator.hasNext()) {
                            try {
                                NewsDetail.ItemBean bean = iterator.next();
                                if (bean.getType().equals(NewsUtils.TYPE_DOC)) {
                                    // 文章类型
                                    if (bean.getStyle().getView() != null) {
                                        if (bean.getStyle().getView().equals(NewsUtils.VIEW_TITLEIMG)) {
                                            // 显示形式单图
                                            bean.itemType = NewsDetail.ItemBean.TYPE_DOC_TITLEIMG;
                                        } else {
                                            bean.itemType = NewsDetail.ItemBean.TYPE_DOC_SLIDEIMG;
                                        }
                                    }
                                } else if (bean.getType().equals(NewsUtils.TYPE_ADVERT)) {
                                    // 广告类型
                                    if (bean.getStyle() != null) {
                                        if (bean.getStyle().getView().equals(NewsUtils.VIEW_TITLEIMG)) {
                                            // 显示形式单图
                                            bean.itemType = NewsDetail.ItemBean.TYPE_ADVERT_TITLEIMG;
                                        } else if (bean.getStyle().getView().equals(NewsUtils.VIEW_SLIDEIMG)) {
                                            // 显示多图
                                            bean.itemType = NewsDetail.ItemBean.TYPE_ADVERT_SLIDEIMG;
                                        } else {
                                            bean.itemType = NewsDetail.ItemBean.TYPE_ADVERT_LONGIMG;
                                        }
                                    } else {
                                        //bean.itemType = NewsDetail.ItemBean.TYPE_ADVERT_TITLEIMG;
                                        iterator.remove();
                                    }
                                } else if (bean.getType().equals(NewsUtils.TYPE_SLIDE)) {
                                    // 图片类型
                                    if (bean.getLink().getType().equals("doc")) {
                                        if (bean.getStyle().getView().equals(NewsUtils.VIEW_SLIDEIMG)) {
                                            bean.itemType = NewsDetail.ItemBean.TYPE_DOC_SLIDEIMG;
                                        } else {
                                            bean.itemType = NewsDetail.ItemBean.TYPE_DOC_TITLEIMG;
                                        }
                                    } else {
                                        bean.itemType = NewsDetail.ItemBean.TYPE_SLIDE;
                                    }
                                } else if (bean.getType().equals(NewsUtils.TYPE_PHVIDEO)) {
                                    //视频类型
                                    bean.itemType = NewsDetail.ItemBean.TYPE_PHVIDEO;
                                } else {
                                    // 凤凰新闻 类型比较多，目前只处理能处理的类型
                                    iterator.remove();
                                }
                            } catch (Exception e) {
                                iterator.remove();
                                e.printStackTrace();
                            }
                        }
                        return newsDetail.getItem();
                    }
                })
                .compose(mView.<List<NewsDetail.ItemBean>>bindToLife())
                .subscribe(new BaseObserver<List<NewsDetail.ItemBean>>() {
                    @Override
                    public void onSuccess(List<NewsDetail.ItemBean> itemBeen) {
                        if (!action.equals("up")) {
                            mView.loadData(itemBeen);
                        } else {
                            mView.loadMoreData(itemBeen);
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        LogUtil.d(TAG, "onFail: " + e.getMessage().toString());
                        if (!action.equals("up")) {
                            mView.loadData(null);
                        } else {
                            mView.loadMoreData(null);
                        }
                    }
                });

    }
}
