package neil.com.daggerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import neil.com.daggerdemo.base.LazyBaseActivity;
import neil.com.daggerdemo.utils.GlideImageLoader;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * App 欢迎页
 *
 * @author neil
 * @date 2018/3/5
 */

public class WelcomeActivity extends LazyBaseActivity {

    @BindView(R.id.gifImageView)
    GifImageView gifImageView;
    @BindView(R.id.iv_ad)
    ImageView ivAd;
    @BindView(R.id.ll_bottom)
    RelativeLayout llBottom;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    @BindView(R.id.fl_ad)
    FrameLayout flAd;
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initToolBar() {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        final GifDrawable gifDrawable = (GifDrawable) gifImageView.getDrawable();
        gifDrawable.setLoopCount(1);
        gifImageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                gifDrawable.start();
            }
        }, 100);

        GlideImageLoader.loadImage(this, "http://api.dujin.org/bing/1920.php", ivAd);

        mCompositeDisposable.add(countDown(3).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                tvSkip.setText("跳过 4");
            }
        }).subscribeWith(new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer integer) {
                tvSkip.setText("跳过 " + (integer + 1));
                tvSkip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onComplete();
                    }
                });
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                toMain();
            }
        }));

    }

    private void toMain() {
        if(mCompositeDisposable != null){
            mCompositeDisposable.dispose();
        }
        Intent intent = new Intent();
        intent.setClass(WelcomeActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


    public Observable<Integer> countDown(int time) {
        if (time < 0) {
            time = 0;
        }
        final int countTime = time;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(Long aLong) throws Exception {
                        return countTime - aLong.intValue();
                    }
                })
                .take(countTime + 1);
    }

    @Override
    protected void onDestroy() {
        if(mCompositeDisposable != null){
            mCompositeDisposable.dispose();
        }
        super.onDestroy();
    }
}
