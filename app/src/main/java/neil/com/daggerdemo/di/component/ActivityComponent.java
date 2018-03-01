package neil.com.daggerdemo.di.component;

import android.app.Activity;
import android.content.Context;

import dagger.Component;
import neil.com.daggerdemo.di.module.ActivityModule;
import neil.com.daggerdemo.di.scope.ContextLife;
import neil.com.daggerdemo.di.scope.PerActivity;
import neil.com.daggerdemo.ui.my.LoginActivity;

/**
 * @author neil
 * @date 2018/3/1
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = ActivityModule.class)
public interface ActivityComponent {

    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    void inject(LoginActivity activity);

}
