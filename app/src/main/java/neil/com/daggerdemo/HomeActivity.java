package neil.com.daggerdemo;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import neil.com.daggerdemo.base.BaseActivity;
import neil.com.daggerdemo.base.BaseFragment;
import neil.com.daggerdemo.ui.home.HomeFragment;
import neil.com.daggerdemo.ui.hotsearch.HotFragment;
import neil.com.daggerdemo.ui.knowledgesystem.KnowledgeSystemFragment;
import neil.com.daggerdemo.ui.my.MyFragment;

/**
 * MainActivity
 *
 * @author neil
 * @date 2018/3/2
 */
@Route(path = "/main/HomeActivity")
public class HomeActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
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
    protected void initView() {
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
        mFragments.add(HotFragment.newInstance());
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
            ToastUtils.showShort("查找");
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
}
