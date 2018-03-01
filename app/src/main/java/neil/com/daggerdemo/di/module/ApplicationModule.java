package neil.com.daggerdemo.di.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import neil.com.daggerdemo.base.App;
import neil.com.daggerdemo.di.scope.ContextLife;
import neil.com.daggerdemo.di.scope.PerApp;

/**
 * @Module 带有此注解的类，用来提供依赖 里面定义一些用@Provides注解的方法,这些方法就是所提供的依赖,
 * Dagger2 会在该类中寻找实例化某个类所需要的依赖
 * Created by neil on 2018/2/28 0028.
 */
@Module
public class ApplicationModule {
    private App mApplication;

    public ApplicationModule(App mApplication) {
        this.mApplication = mApplication;
    }

    @Provides
    @PerApp
    @ContextLife("Application")
    public Context provideApplicationContext(){
        return mApplication.getApplicationContext();
    }
}
