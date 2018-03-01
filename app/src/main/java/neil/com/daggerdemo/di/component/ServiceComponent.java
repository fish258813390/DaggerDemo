package neil.com.daggerdemo.di.component;

import android.content.Context;

import dagger.Component;
import neil.com.daggerdemo.di.module.ServiceModule;
import neil.com.daggerdemo.di.scope.ContextLife;
import neil.com.daggerdemo.di.scope.PerService;

/**
 * Created by neil on 2018/3/1 0001.
 */
@PerService
@Component(dependencies = ApplicationComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {

    @ContextLife("Service")
    Context getServiceContext();

    @ContextLife("Application")
    Context getApplicationContext();
}
