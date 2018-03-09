package neil.com.daggerdemo.ui.video.presenter;

import java.util.List;

import javax.inject.Inject;

import neil.com.daggerdemo.base.BasePresenter;
import neil.com.daggerdemo.bean.VideoChannelBean;
import neil.com.daggerdemo.bean.VideoDetailBean;
import neil.com.daggerdemo.constant.Constant;
import neil.com.daggerdemo.net.ApiService;
import neil.com.daggerdemo.net.BaseObserver;
import neil.com.daggerdemo.net.RetrofitManager;
import neil.com.daggerdemo.ui.video.contract.VideoContract;
import neil.com.daggerdemo.utils.RxSchedulers;

/**
 * @author neil
 * @date 2018/3/9
 */

public class VideoPresenter extends BasePresenter<VideoContract.View> implements VideoContract.Presenter {

    @Inject
    public VideoPresenter() {
    }

    @Override
    public void getViewChannel() {
        RetrofitManager.create(ApiService.class, Constant.sIFengApi)
                .getVideoChannel(1)
                .compose(RxSchedulers.<List<VideoChannelBean>>applySchedulers())
                .compose(mView.<List<VideoChannelBean>>bindToLife())
                .subscribe(new BaseObserver<List<VideoChannelBean>>() {
                    @Override
                    public void onSuccess(List<VideoChannelBean> channelBeans) {
                        mView.loadVideoChannel(channelBeans);
                    }

                    @Override
                    public void onFail(Throwable e) {

                    }
                });

    }

    @Override
    public void getVideoDetails(final int page, String listType, String typeId) {
        RetrofitManager.create(ApiService.class, Constant.sIFengApi)
                .getVideoDetail(page, listType, typeId)
                .compose(RxSchedulers.<List<VideoDetailBean>>applySchedulers())
                .compose(mView.<List<VideoDetailBean>>bindToLife())
                .subscribe(new BaseObserver<List<VideoDetailBean>>() {
                    @Override
                    public void onSuccess(List<VideoDetailBean> detailBeanList) {
                        if (page > 1) {
                            mView.loadMoreVideoDetails(detailBeanList);
                        } else {
                            mView.loadVideoDetails(detailBeanList);
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {

                    }
                });

    }
}
