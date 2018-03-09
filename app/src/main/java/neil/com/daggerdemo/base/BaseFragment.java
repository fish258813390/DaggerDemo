package neil.com.daggerdemo.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.annotations.Nullable;
import neil.com.daggerdemo.R;
import neil.com.daggerdemo.constant.Constant;
import neil.com.daggerdemo.constant.LoadType;
import neil.com.daggerdemo.di.component.DaggerFragmentComponent;
import neil.com.daggerdemo.di.component.FragmentComponent;
import neil.com.daggerdemo.di.module.FragmentModule;
import neil.com.daggerdemo.utils.DialogHelper;
import neil.com.daggerdemo.widget.MultiStateView;
import neil.com.daggerdemo.widget.SimpleMultiStateView;

/**
 * Fragment 基类
 * <p>
 * onCreateView --> onViewCreated
 *
 * @author neil
 * @date 2018/3/2
 */

public abstract class BaseFragment<T extends BaseContract.BasePresenter> extends RxFragment implements BaseContract.BaseView {

    @Nullable
    @Inject
    protected T mPresenter;
    protected FragmentComponent mFragmentComponent;

    private Unbinder unbinder;
    private View mRootView, mErrorView, mEmptyView;
    protected Context mContext;
    protected Dialog mLoadingDialog = null;

    @android.support.annotation.Nullable
    @BindView(R.id.SimpleMultiStateView)
    SimpleMultiStateView mSimpleMultiStateView;

    protected abstract void initInjector();

    protected abstract int getLayoutId();

    protected abstract void initView(View view);

    @android.support.annotation.Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @android.support.annotation.Nullable ViewGroup container, @android.support.annotation.Nullable Bundle savedInstanceState) {
        if (mRootView != null) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
        } else {
            mRootView = inflaterView(inflater, container);
        }
        mContext = mRootView.getContext();
        mLoadingDialog = DialogHelper.getLoadingDialog(getActivity());
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFragmentComponent();
        ARouter.getInstance().inject(this);
        initInjector();
        attachView();
        initView(mRootView);
        if (!NetworkUtils.isConnected()) {
            showNoNet();
        }
        initStateView();
    }

    /**
     * 加载view
     *
     * @param inflater
     * @param container
     */
    private View inflaterView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    /**
     * 初始化Component,完成 fragment 依赖实例的注入
     */
    private void initFragmentComponent() {
        mFragmentComponent = DaggerFragmentComponent.builder()
                .applicationComponent(((App) getActivity().getApplication()).getApplicationComponent())
                .fragmentModule(new FragmentModule(this))
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        detachView();
    }

    @Override
    public void showLoading() {
        ToastUtils.showShort("showLoading");
    }

    @Override
    public void hideLoading() {
        ToastUtils.showShort("hideLoading");
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showLoadingView();
        }
    }

    @Override
    public void showSuccess(String successMsg) {
        hideLoadingDialog();
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showContent();
        }
    }

    @Override
    public void showFaild(String errorMsg) {
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showErrorView();
        }
    }

    @Override
    public void showNoNet() {
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showNoNetView();
        }
    }

    @Override
    public void onRetry() {
        ToastUtils.showShort("onRetry");
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }


    /**
     * 设置加载数据结果
     *
     * @param baseQuickAdapter
     * @param refreshLayout
     * @param list
     * @param loadType
     */
    protected void setLoadDataResult(
            BaseQuickAdapter baseQuickAdapter, SwipeRefreshLayout refreshLayout, List list, @LoadType.checker int loadType) {
        switch (loadType) {
            case LoadType.TYPE_REFRESH_SUCCESS:
                baseQuickAdapter.setNewData(list);
                refreshLayout.setRefreshing(false);
                break;

            case LoadType.TYPE_REFRESH_ERROR:
                refreshLayout.setRefreshing(false);
                break;

            case LoadType.TYPE_LOAD_MORE_SUCCESS:
                if (null != list) {
                    baseQuickAdapter.addData(list);
                }
                break;

            case LoadType.TYPE_LOAD_MORE_ERROR:
                baseQuickAdapter.loadMoreFail();
                break;
        }
        if (list == null || list.isEmpty() || list.size() < Constant.PAGE_SIZE) {
            baseQuickAdapter.loadMoreEnd(false);
        } else {
            baseQuickAdapter.loadMoreComplete();
        }
    }

    // 初始化状态加载view
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

    protected void showLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.show();
        }
    }

    protected void showLoadingDialog(String str) {
        if (mLoadingDialog != null) {
            TextView tv = mLoadingDialog.findViewById(R.id.tv_load_dialog);
            tv.setText(str);
            mLoadingDialog.show();
        }
    }

    protected void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
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
}
