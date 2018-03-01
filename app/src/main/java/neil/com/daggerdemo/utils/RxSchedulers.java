package neil.com.daggerdemo.utils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 通用的Rx线程转换类
 * @author neil
 * @date 2018/3/1
 */
public class RxSchedulers {

    static final ObservableTransformer schedulerThransformer = new ObservableTransformer() {
        @Override
        public ObservableSource apply(Observable upstream) {
            return upstream.subscribeOn(Schedulers.io()) // 自身在哪个调度器上执行
                    .observeOn(AndroidSchedulers.mainThread()); // 一个观察者在哪个调度器上订阅observable
        }
    };

    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return (ObservableTransformer<T, T>) schedulerThransformer;
    }

}
