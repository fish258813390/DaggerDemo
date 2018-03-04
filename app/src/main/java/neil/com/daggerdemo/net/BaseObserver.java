package neil.com.daggerdemo.net;


import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by neil on 2018/3/3 0003.
 */

public abstract class BaseObserver<T> implements Observer<T> {

    public abstract void onSuccess(T t);

    public abstract void onFail(Throwable e);

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        onFail(e);
    }

    @Override
    public void onComplete() {

    }
}
