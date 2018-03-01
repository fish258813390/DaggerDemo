package neil.com.daggerdemo.di.module;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import dagger.Module;
import dagger.Provides;
import neil.com.daggerdemo.di.scope.ContextLife;
import neil.com.daggerdemo.di.scope.PerFragment;

/**
 * @author neil
 * @date 2018/3/1
 */
@Module
public class FragmentModule {

    private Fragment mFragment;

    public FragmentModule(Fragment mFragment) {
        this.mFragment = mFragment;
    }

    @Provides
    @PerFragment
    @ContextLife("Activity")
    public Context provideActivityContext() {
        return mFragment.getActivity();
    }

    @Provides
    @PerFragment
    public Activity provideActivity() {
        return mFragment.getActivity();
    }

    @Provides
    @PerFragment
    public Fragment provideFragment() {
        return mFragment;
    }

}
