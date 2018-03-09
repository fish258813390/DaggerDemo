package neil.com.daggerdemo;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import neil.com.daggerdemo.base.BaseActivity;
import neil.com.daggerdemo.base.BaseFragment;
import neil.com.daggerdemo.base.LazyBaseActivity;
import neil.com.daggerdemo.base.LazyBaseFragment;
import neil.com.daggerdemo.ui.home.HomeFragment;
import neil.com.daggerdemo.ui.hotsearch.HotFragment;
import neil.com.daggerdemo.ui.knowledgesystem.KnowledgeSystemFragment;
import neil.com.daggerdemo.ui.my.MyFragment;
import neil.com.daggerdemo.ui.news.NewsFragment;
import neil.com.daggerdemo.ui.video.VideoFragment;

/**
 * MainActivity
 *
 * @author neil
 * @date 2018/3/2
 */
@Route(path = "/main/HomeActivity")
public class HomeActivity extends LazyBaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    @Autowired
    public String title;
    @Autowired
    public String type;

    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;
    private List<BaseFragment> mFragments;
    private int mLastFgIndex;
    private long mExitTime;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar == null) {
            throw new NullPointerException("toolbar can not be null");
        }
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(showHomeAsUp());
        /**toolbar除掉阴影*/
        getSupportActionBar().setElevation(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setElevation(0);
        }
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mNavigation.setOnNavigationItemSelectedListener(this);
        initFragment();
        switchFragment(0);
        LogUtils.d("params",title + "::::" + type);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.navigation_home:
                mToolbar.setTitle(R.string.app_name);
                switchFragment(0);
                break;

            case R.id.navigation_knowledgesystem:
                mToolbar.setTitle(R.string.title_knowledgesystem);
                switchFragment(1);
                break;

            case R.id.navigation_my:
                mToolbar.setTitle(R.string.title_my);
                switchFragment(2);
                break;
        }

        return true;
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        mFragments = new ArrayList<>();
        mFragments.add(HomeFragment.newInstance());
        mFragments.add(KnowledgeSystemFragment.newInstance());
        mFragments.add(MyFragment.newInstance());
//        mFragments.add(HotFragment.newInstance());
        mFragments.add(NewsFragment.newInstance());
        mFragments.add(VideoFragment.newInstance());
    }

    /**
     * 切换fragment
     *
     * @param position 要显示的fragment的下标
     */
    private void switchFragment(int position) {
        if (position > mFragments.size()) {
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment targetFg = mFragments.get(position);
        Fragment lastFg = mFragments.get(mLastFgIndex);
        mLastFgIndex = position;
        ft.hide(lastFg);
        if (!targetFg.isAdded()) {
            ft.add(R.id.layout_fragment, targetFg);
        }
        ft.show(targetFg);
        ft.commitAllowingStateLoss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuHot) {
            switchFragment(3);
        } else if (item.getItemId() == R.id.menuSearch) {
            switchFragment(4);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if ((System.currentTimeMillis() - mExitTime) > 2000) {
//                ToastUtils.showShort(R.string.exit_system);
//                mExitTime = System.currentTimeMillis();
//            } else {
//                this.finish();
//            }
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }
}
