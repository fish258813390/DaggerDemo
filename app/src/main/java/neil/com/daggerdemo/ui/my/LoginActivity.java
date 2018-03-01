package neil.com.daggerdemo.ui.my;

import android.support.design.widget.TextInputEditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import neil.com.daggerdemo.R;
import neil.com.daggerdemo.base.BaseActivity;
import neil.com.daggerdemo.bean.User;
import neil.com.daggerdemo.constant.Constant;
import neil.com.daggerdemo.event.LoginEvent;
import neil.com.daggerdemo.utils.RxBus;

/**
 * @author neil
 * @date 2018/3/1
 */
@Route(path = "/my/LoginActivity")
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {
    @BindView(R.id.etUsername)
    TextInputEditText mEtUsername;
    @BindView(R.id.etPassword)
    TextInputEditText mEtPassword;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected void initView() {
//        mEtUsername.setText(SPUtils.getInstance(Constant.SHARED_NAME).getString(Constant.USERNAME_KEY));
        mEtUsername.setText("fish19920404");
//        mEtPassword.setText(SPUtils.getInstance(Constant.SHARED_NAME).getString(Constant.PASSWORD_KEY));
        mEtPassword.setText("fish19920404");
    }

    @Override
    protected boolean showHomeAsUp() {
        return true;
    }

    @OnClick(R.id.btnLogin)
    public void login() {
        String username = mEtUsername.getText().toString();
        String password = mEtPassword.getText().toString();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            ToastUtils.showShort(R.string.the_username_or_password_can_not_be_empty);
            return;
        }
        mPresenter.login(username, password);
    }

    @Override
    public void loginSuccess(User user) {
        ToastUtils.showShort("登录成功~!");
        RxBus.getInstance().post(new LoginEvent("login","0000"));
        finish();
    }

    public static void start() {
        ARouter.getInstance().build("/my/LoginActivity").navigation();
    }
}
