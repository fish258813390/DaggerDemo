package neil.com.daggerdemo.ui.my;

import neil.com.daggerdemo.base.BaseContract;
import neil.com.daggerdemo.bean.User;

/**
 * 登录契约类
 *
 * @author neil
 * @date 2018/3/1
 */
public interface LoginContract {

    interface View extends BaseContract.BaseView {
        void loginSuccess(User user);
    }

    interface Presenter extends BaseContract.BasePresenter<LoginContract.View> {

        /**
         * 登录
         * @param username
         * @param password
         */
        void login(String username, String password);

    }

}
