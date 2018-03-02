package neil.com.daggerdemo.base;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.Utils;

import org.litepal.LitePal;

import neil.com.daggerdemo.BuildConfig;
import neil.com.daggerdemo.di.component.ApplicationComponent;
import neil.com.daggerdemo.di.component.DaggerApplicationComponent;
import neil.com.daggerdemo.di.module.ApplicationModule;

/**
 * Created by neil on 2018/2/28 0028.
 */
public class App extends Application {
    private ApplicationComponent mApplicationComponent;
    private static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initApplicationComponent();
        Utils.init(this);
        intARouter();
        // 初始化db
        LitePal.initialize(this);

    }

    /**
     * 初始化ApplicationComponent
     */
    private void initApplicationComponent() {
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }


    /**
     * 初始化路由 ARouter
     */
    private void intARouter() {
        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    public static Context getAppContext(){
        return mInstance.getApplicationContext();
    }

    public static App getmInstance() {
        return mInstance;
    }
}
