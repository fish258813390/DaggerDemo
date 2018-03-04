package neil.com.daggerdemo.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by lw on 2017-09-01.
 */

public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        Glide.with(context.getApplicationContext())
                .load(path)
                .into(imageView);
    }

    /**
     * 常规使用
     *
     * @param context   上下文
     * @param url       图片链接
     * @param imageView 目标view
     */
    public static void loadImage(Context context, Object url, ImageView imageView) {
        Glide.with(context).load(url)
                .apply(new RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .transition(new DrawableTransitionOptions().crossFade(800))
                .into(imageView);
    }


    /**
     * 自定义RequestOptions使用
     *
     * @param context        上下文
     * @param url            图片链接
     * @param requestOptions
     * @param imageView      目标view
     */
    public static void loadImage(Context context, Object url, ImageView imageView, RequestOptions requestOptions) {
        Glide.with(context).load(url)
                .apply(requestOptions)
                .transition(new DrawableTransitionOptions().crossFade(800))
                .into(imageView);
    }

    /**
     * 自定义RequestOptions使用
     *
     * @param fragment
     * @param url            图片链接
     * @param requestOptions
     * @param imageView      目标view
     */
    public static void loadImage(android.support.v4.app.Fragment fragment, Object url, ImageView imageView, RequestOptions requestOptions) {
        Glide.with(fragment).load(url)
                .apply(requestOptions)
                .transition(new DrawableTransitionOptions().crossFade(800))
                .into(imageView);
    }


    /**
     * 需要回调时使用
     *
     * @param context         上下文
     * @param url             图片链接
     * @param imageViewTarget 回调需求
     */
    public static void loadImage(Context context, Object url, ImageViewTarget imageViewTarget) {
        Glide.with(context).load(url)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .transition(new DrawableTransitionOptions().crossFade(800))
                .into(imageViewTarget);
    }

    /**
     * 需要回调时使用
     *
     * @param context   上下文
     * @param url       图片链接
     * @param imageView 回调需求
     */
    public static void loadImage(Context context, Object url, ImageView imageView, RequestListener listener) {
        Glide.with(context).load(url)
                //.thumbnail(0.1f)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .transition(new DrawableTransitionOptions().crossFade(800))
                .listener(listener)
                .into(imageView);
    }

}
