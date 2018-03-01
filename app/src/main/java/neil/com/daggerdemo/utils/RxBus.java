package neil.com.daggerdemo.utils;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * @author neil
 * @date 2018/3/1
 */

public class RxBus {

    private static volatile RxBus sRxBus;
    private final FlowableProcessor<Object> mBus;

    /**
     * PublishSubject 只会把在订阅发生的时间点之后来自原始的Observable的数据发射给观察者
     */
    public RxBus() {
        mBus = PublishProcessor.create().toSerialized();
    }

    public static RxBus getInstance() {
        if (sRxBus == null) {
            synchronized (RxBus.class) {
                if (sRxBus == null) {
                    sRxBus = new RxBus();
                }
            }
        }
        return sRxBus;
    }

    /**
     * 发送一个新的事件
     * @param o
     */
    public void post(Object o) {
        mBus.onNext(o);
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的被观察者
     *
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> Flowable<T> toFlowable(Class<T> eventType) {
        return mBus.ofType(eventType);
    }

}
