package neil.com.daggerdemo.base;

/**
 * Presenter 基类
 * 必须是在契约类下 共存的view presenter关系
 *
 * @author neil
 * @date 2018/3/1
 */
public class BasePresenter<T extends BaseContract.BaseView> implements BaseContract.BasePresenter<T> {

    protected T mView;

    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        if (null != mView) {
            mView = null;
        }
    }
}
