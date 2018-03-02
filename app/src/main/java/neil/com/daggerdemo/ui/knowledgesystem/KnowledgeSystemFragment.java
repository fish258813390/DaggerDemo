package neil.com.daggerdemo.ui.knowledgesystem;

import android.view.View;

import neil.com.daggerdemo.R;
import neil.com.daggerdemo.base.BaseFragment;

/**
 * @author neil
 * @date 2018/3/2
 */

public class KnowledgeSystemFragment extends BaseFragment {
    @Override
    protected void initInjector() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_knowledge_system;
    }

    @Override
    protected void initView(View view) {

    }

    public static KnowledgeSystemFragment newInstance() {
        return new KnowledgeSystemFragment();
    }
}
