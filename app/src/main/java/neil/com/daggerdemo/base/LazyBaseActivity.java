package neil.com.daggerdemo.base;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import io.reactivex.annotations.Nullable;
import neil.com.daggerdemo.R;
import neil.com.daggerdemo.di.component.ActivityComponent;
import neil.com.daggerdemo.di.component.DaggerActivityComponent;
import neil.com.daggerdemo.di.module.ActivityModule;
import neil.com.daggerdemo.utils.DialogHelper;
import neil.com.daggerdemo.widget.MultiStateView;
import neil.com.daggerdemo.widget.SimpleMultiStateView;

/**
 * Activity 基类
 * 1.依附Presenter
 * 依附时需要实现契约类中的回调View
 *
 * @author neil
 * @Inject 带有此注解的属性或构造方法将参与依赖注入中, Dagger2会实例化有此注解的类
 * @date 2018/3/1
 */
public abstract class LazyBaseActivity<T1 extends BaseContract.BasePresenter> extends SupportActivity
        implements BaseContract.BaseView, BGASwipeBackHelper.Delegate {

    protected View mRootView;

    @Nullable
    @Inject
    protected T1 mPresenter;
    protected ActivityComponent mActivityComponent;
    protected Toolbar mToolbar;
    private Unbinder unbinder;
    protected Dialog mLoadingDialog = null;

    @android.support.annotation.Nullable
    @BindView(R.id.SimpleMultiStateView)
    SimpleMultiStateView mSimpleMultiStateView;

    protected BGASwipeBackHelper mSwipeBackHelper;

    protected abstract int getLayoutId();

    protected abstract void initInjector();

    protected abstract void initToolBar();

    protected abstract void initView(View view,Bundle savedInstanceState);

    @Override
    protected void onCreate(@android.support.annotation.Nullable Bundle savedInstanceState) {
        initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        mRootView = inflaterView(null, null, savedInstanceState);
        setContentView(mRootView);
        initActivityComponent();
        ARouter.getInstance().inject(this);
        initInjector();
        initToolBar();
        attachView();
        initView(mRootView,savedInstanceState);
        if (!NetworkUtils.isConnected()) {
            showNoNet();
        }
        mLoadingDialog = DialogHelper.getLoadingDialog(this);
    }

    /**
     * 加载view
     *
     * @param inflater
     * @param container
     */
    private View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(getLayoutId(), container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    /**
     * 初始化Component,完成 子Activity 依赖实例的注入
     */
    private void initActivityComponent() {
        mActivityComponent = DaggerActivityComponent.builder().applicationComponent(((App) getApplication()).getApplicationComponent())
                .activityModule(new ActivityModule(this)).build();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        detachView();
    }

    /**
     * 是否显示返回键
     *
     * @return
     */
    protected boolean showHomeAsUp() {
        return false;
    }

    @Override
    public void showLoading() {
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showLoadingView();
        }
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showSuccess(String successMsg) {
        ToastUtils.showShort(successMsg);
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showContent();
        }
    }

    @Override
    public void showFaild(String errorMsg) {
        ToastUtils.showShort(errorMsg);
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showErrorView();
        }
    }

    @Override
    public void showNoNet() {
        ToastUtils.showShort(R.string.no_network_connection);
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showNoNetView();
        }
    }

    @Override
    public void onRetry() {

    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 贴上View
     */
    private void attachView() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    /**
     * 分离View
     */
    private void detachView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    private void initStateView() {
        if (mSimpleMultiStateView == null) {
            return;
        }
        mSimpleMultiStateView.setEmptyResource(R.layout.view_empty)
                .setRetryResource(R.layout.view_retry)
                .setLoadingResource(R.layout.view_loading)
                .setNoNetResource(R.layout.view_nonet)
                .build()
                .setonReLoadlistener(new MultiStateView.onReLoadlistener() {
                    @Override
                    public void onReload() {
                        onRetry();
                    }
                });
    }

    protected void setToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);
        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackManager.getInstance().init(this) 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。
        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(false);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
    }

    protected SimpleMultiStateView getStateView() {
        return mSimpleMultiStateView;
    }

    protected void showLoadingDialog() {
        if (mLoadingDialog != null)
            mLoadingDialog.show();
    }

    protected void showLoadingDialog(String str) {
        if (mLoadingDialog != null) {
            TextView tv = (TextView) mLoadingDialog.findViewById(R.id.tv_load_dialog);
            tv.setText(str);
            mLoadingDialog.show();
        }
    }

    protected void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    // 手势退出activity

    /**
     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return
     */
    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    @Override
    public void onSwipeBackLayoutSlide(float v) {

    }

    @Override
    public void onSwipeBackLayoutCancel() {

    }

    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }
}
