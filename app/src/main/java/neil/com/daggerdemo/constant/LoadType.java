package neil.com.daggerdemo.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 加载状态
 * @author neil
 * @date 2018/3/2
 */

public class LoadType {

    public static final int TYPE_REFRESH_SUCCESS = 1;
    public static final int TYPE_REFRESH_ERROR = 2;
    public static final int TYPE_LOAD_MORE_SUCCESS = 3;
    public static final int TYPE_LOAD_MORE_ERROR = 4;

    // 不用枚举,使用注解
    @IntDef({TYPE_REFRESH_SUCCESS,TYPE_REFRESH_ERROR,TYPE_LOAD_MORE_SUCCESS,TYPE_LOAD_MORE_ERROR})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface  checker{

    }

}
