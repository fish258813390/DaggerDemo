package neil.com.daggerdemo.di.module;

import android.app.Service;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import neil.com.daggerdemo.di.scope.ContextLife;
import neil.com.daggerdemo.di.scope.PerService;

/**
 * @author neil
 * @date 2018/3/1
 */
@Module
public class ServiceModule {

    private Service mService;

    public ServiceModule(Service service) {
        this.mService = service;
    }

    @Provides
    @PerService
    @ContextLife("Service")
    public Context provideServiceContext() {
        return mService;
    }
}
