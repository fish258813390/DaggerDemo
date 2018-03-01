package neil.com.daggerdemo.di.module;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import neil.com.daggerdemo.di.scope.ContextLife;
import neil.com.daggerdemo.di.scope.PerActivity;

/**
 * @author neil
 * @date 2018/3/1
 */
@Module
public class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @PerActivity
    @ContextLife("Activity")
    public Context provideActivityContext(){
        return mActivity;
    }

    @Provides
    @PerActivity
    public Activity provideActivity(){
        return mActivity;
    }


}
