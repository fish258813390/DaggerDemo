package neil.com.daggerdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import neil.com.daggerdemo.event.LoginEvent;
import neil.com.daggerdemo.utils.RxBus;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_result)
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ARouter.getInstance().build("/my/LoginActivity").navigation();
                ARouter.getInstance().build("/main/HomeActivity")
                        .withString("title","home")
                        .withString("type","1")
                        .navigation();
            }
        });


        RxBus.getInstance().toFlowable(LoginEvent.class)
                .subscribe(new Consumer<LoginEvent>() {
                    @Override
                    public void accept(LoginEvent loginEvent) throws Exception {
                        LogUtils.d("MainActivity----收到----登录成功"+ new Gson().toJson(loginEvent));
                        ToastUtils.showShort("收到-----揭露成功");
                        tvResult.setText("登录陈宫" + loginEvent.toString());
                    }
                });
    }
}
