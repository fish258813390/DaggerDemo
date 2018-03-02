package neil.com.daggerdemo.ui.my;

import android.view.View;

import neil.com.daggerdemo.R;
import neil.com.daggerdemo.base.BaseFragment;

/**
 * 我的
 * @author neil
 * @date 2018/3/2
 */

public class MyFragment extends BaseFragment {
    @Override
    protected void initInjector() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView(View view) {

    }

    public static MyFragment newInstance() {
        return new MyFragment();
    }
}
