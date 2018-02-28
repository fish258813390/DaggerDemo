package neil.com.daggerdemo.di.component;

import android.content.Context;

import dagger.Component;
import neil.com.daggerdemo.di.module.ApplicationModule;

/**
 * Created by neil on 2018/2/28 0028.
 */
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Context getApplication();

}
