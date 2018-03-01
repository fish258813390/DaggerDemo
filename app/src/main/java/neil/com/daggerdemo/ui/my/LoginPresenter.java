package neil.com.daggerdemo.ui.my;

import com.blankj.utilcode.util.LogUtils;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import neil.com.daggerdemo.base.BasePresenter;
import neil.com.daggerdemo.bean.DataResponse;
import neil.com.daggerdemo.bean.User;
import neil.com.daggerdemo.net.ApiService;
import neil.com.daggerdemo.net.RetrofitManager;
import neil.com.daggerdemo.utils.RxSchedulers;

/**
 * LoginPresenter
 *
 * @author neil
 * @date 2018/3/1
 */
public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    @Inject
    public LoginPresenter() {
        LogUtils.d("初始化城欧共");
    }

    @Override
    public void login(String username, String password) {
        RetrofitManager.create(ApiService.class)
                .login(username, password)
                .compose(RxSchedulers.<DataResponse<User>>applySchedulers())
                .compose(mView.<DataResponse<User>>bindToLife())
                .subscribe(new Consumer<DataResponse<User>>() {
                    @Override
                    public void accept(DataResponse<User> response) throws Exception {
                        if (response.getErrorCode() == 0) {
                            mView.loginSuccess(response.getData());
                        } else {
                            mView.showFaild(String.valueOf(response.getErrorMsg()));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showFaild(throwable.getMessage());
                    }
                });
    }


}
