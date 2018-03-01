package neil.com.daggerdemo.event;

/**
 * 登录成功 事件event
 * Created by lw on 2018/1/25.
 */
public class LoginEvent {

    private String type;

    private String resultCode;

    public LoginEvent(String type, String resultCode) {
        this.type = type;
        this.resultCode = resultCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    @Override
    public String toString() {
        return "LoginEvent{" +
                "type='" + type + '\'' +
                ", resultCode='" + resultCode + '\'' +
                '}';
    }
}
