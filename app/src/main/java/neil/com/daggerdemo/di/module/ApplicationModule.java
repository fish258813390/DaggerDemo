package neil.com.daggerdemo.di.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import neil.com.daggerdemo.base.App;

/**
 * Created by neil on 2018/2/28 0028.
 */
@Module
public class ApplicationModule {
    private App mApplication;

    public ApplicationModule(App mApplication) {
        this.mApplication = mApplication;
    }

    @Provides
    public Context provideApplicationContext(){
        return mApplication.getApplicationContext();
    }
}
