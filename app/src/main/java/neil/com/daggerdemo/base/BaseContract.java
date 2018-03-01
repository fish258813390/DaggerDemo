package neil.com.daggerdemo.base;

import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * 基本契约类
 *
 * @author neil
 * @date 2018/3/1
 */
public interface BaseContract {

    interface BasePresenter<T extends BaseContract.BaseView> {

        /**
         * 附加到View 层
         * @param view
         */
        void attachView(T view);

        /**
         * 与View层分离
         */
        void detachView();
    }

    interface BaseView {

        /**
         * 加载中
         */
        void showLoading();

        /**
         * 隐藏进度
         */
        void hideLoading();

        /**
         * 显示请求成功
         * @param successMsg
         */
        void showSuccess(String successMsg);

        /**
         * 失败重试
         * @param errorMsg
         */
        void showFaild(String errorMsg);

        /**
         * 显示当前网络不可用
         */
        void showNoNet();

        /**
         * 重试
         */
        void onRetry();

        /**
         * 绑定view 生命周期
         */
        <T> LifecycleTransformer<T> bindToLife();
    }

}
