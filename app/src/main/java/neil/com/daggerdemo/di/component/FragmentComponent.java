package neil.com.daggerdemo.di.component;

import android.app.Activity;
import android.content.Context;

import dagger.Component;
import neil.com.daggerdemo.di.module.FragmentModule;
import neil.com.daggerdemo.di.scope.ContextLife;
import neil.com.daggerdemo.di.scope.PerFragment;
import neil.com.daggerdemo.ui.home.HomeFragment;
import neil.com.daggerdemo.ui.news.DetailFragment;
import neil.com.daggerdemo.ui.news.NewsFragment;
import neil.com.daggerdemo.ui.video.VideoDetailFragment;
import neil.com.daggerdemo.ui.video.VideoFragment;

/**
 * @author neil
 * @date 2018/3/1
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    void inject(HomeFragment fragment);

    void inject(NewsFragment fragment);

    void inject(DetailFragment fragment);

    void inject(VideoFragment fragment);

    void inject(VideoDetailFragment fragment);

}
