package neil.com.daggerdemo.di.component;

import android.content.Context;

import dagger.Component;
import neil.com.daggerdemo.di.module.ApplicationModule;
import neil.com.daggerdemo.di.scope.ContextLife;
import neil.com.daggerdemo.di.scope.PerApp;

/**
 * Created by neil on 2018/2/28 0028.
 */
@PerApp
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ContextLife("Application")
    Context getApplication();

}
