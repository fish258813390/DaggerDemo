package neil.com.daggerdemo.ui.hotsearch;

import android.view.View;

import neil.com.daggerdemo.R;
import neil.com.daggerdemo.base.BaseFragment;

/**
 * @author neil
 * @date 2018/3/2
 */

public class HotFragment extends BaseFragment {

    @Override
    protected void initInjector() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hot;
    }

    @Override
    protected void initView(View view) {

    }

    public static HotFragment newInstance() {
        return new HotFragment();
    }
}
