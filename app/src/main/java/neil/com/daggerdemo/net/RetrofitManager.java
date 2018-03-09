package neil.com.daggerdemo.net;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import neil.com.daggerdemo.base.App;
import neil.com.daggerdemo.constant.Constant;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author neil
 * @date 2018/3/1
 */
public class RetrofitManager {

    private static long CONNECT_TIMEOUT = 60L;
    private static long READ_TIMEOUT = 10L;
    private static long WRITE_TIMEOUT = 10L;
    //设缓存有效期为1天
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    public static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置
    //(假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)
    public static final String CACHE_CONTROL_NETWORK = "Cache-Control: public, max-age=10";
    // 避免出现 HTTP 403 Forbidden，参考：http://stackoverflow.com/questions/13670692/403-forbidden-with-java-but-not-web-browser
    private static final String AVOID_HTTP403_FORBIDDEN = "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";
    private static volatile OkHttpClient mOkHttpClient;

    /**
     * 获取OkHttpClient实例
     * 待优化
     * @return
     */
    private static OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (RetrofitManager.class) {
//                Cache cache = new Cache(new File(App.getAppContext().getCacheDir(), "HttpCache"), 1024 * 1024 * 100);
                if (mOkHttpClient == null) {
                    mOkHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                            .addInterceptor(mRewriteCacheControlInterceptor)
                            .addInterceptor(mLoggingInterceptor)
                            .addInterceptor(sQueryParameterInterceptor)
//                            .cookieJar(new CookiesManager())
                            .build();
                }
            }
        }
        return mOkHttpClient;
    }

    /**
     * 获取Service
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T create(Class<T> clazz) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.REQUEST_BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        return retrofit.create(clazz);
    }

    /**
     * 获取Service
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T create(Class<T> clazz, String url) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        return retrofit.create(clazz);
    }


    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private static final Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetworkUtils.isConnected()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            if (NetworkUtils.isConnected()) {
                //有网的时候读接口上的@Headers里的配置，可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_CONTROL_CACHE)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };

    /**
     * 日志拦截器
     */
    private static final Interceptor mLoggingInterceptor = new Interceptor() {

        String TAG = "LoggerInterceptor";
        private String content;

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            LogUtils.json(TAG, "request:" + request.url());
            Headers headers = request.headers();
            for (int i = 0; i < headers.size(); i++) {
                String headerName = headers.name(i);
                String headerValue = headers.get(headerName);
//                LogUtils.json(TAG, "Header----------->Name:" + headerName + "------------>Value:" + headerValue + "\n");
            }
            RequestBody requestBody = request.body();
            if (requestBody instanceof FormBody) {
                HashMap<String, Object> rootMap = new HashMap<>();
                for (int i = 0; i < ((FormBody) requestBody).size(); i++) {
                    rootMap.put(((FormBody) requestBody).encodedName(i), getValueDecode(((FormBody) requestBody).encodedValue(i)));
                }
                LogUtils.json(TAG, "params : " + new Gson().toJson(rootMap));
            }
            long t1 = System.nanoTime();
            okhttp3.Response response = chain.proceed(chain.request());
            okhttp3.MediaType mediaType = response.body().contentType();
            ResponseBody originalBody = response.body();
            if (null != originalBody) {
                content = originalBody.string();
            }
            LogUtils.json(TAG, "response body:" + content);
            try {

            } catch (Exception e) {
                LogUtils.json("Exception", e.getMessage());
                e.printStackTrace();
            }
            long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - t1);
//            LogUtils.json(TAG, "time : " + " (" + tookMs + "ms" + ')');
//            return response;
            return response.newBuilder().body(ResponseBody.create(mediaType, content)).build();
        }
    };

    /**
     * 解决中文乱码结果集
     *
     * @param value
     * @return
     */
    private static String getValueDecode(String value) {
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 添加查询参数: 公共参数
     * 方式一
     */
    public static final Interceptor sQueryParameterInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request request;
            HttpUrl modifiedUrl = originalRequest.url().newBuilder()
                    .addQueryParameter("uid", Constant.uid)
                    .addQueryParameter("devid", Constant.uid)
                    .addQueryParameter("proid", "ifengnews")
                    .addQueryParameter("vt", "5")
                    .addQueryParameter("publishid", "6103")
                    .addQueryParameter("screen", "1080x1920")
                    .addQueryParameter("df", "androidphone")
                    .addQueryParameter("os", "android_22")
                    .addQueryParameter("nw", "wifi")
                    .build();
            request = originalRequest.newBuilder().url(modifiedUrl).build();
            return chain.proceed(request);
        }
    };

    /**
     * 添加公共头信息
     * 可放入公共请求头中
     */
    private class AddHeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder()
                    .header("register_platform", "")
                    .build();
            return chain.proceed(request);
        }
    }

    /**
     * 请求体定制：统一添加定制参数
     */
    private class AddBodyInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder();
            if (original.body() instanceof FormBody) {
                FormBody.Builder newFormBody = new FormBody.Builder();
                FormBody oidFormBody = (FormBody) original.body();
                for (int i = 0; i < oidFormBody.size(); i++) {
                    newFormBody.addEncoded(oidFormBody.encodedName(i), oidFormBody.encodedValue(i));
                }
                newFormBody.add("register_platform", "");
                requestBuilder.method(original.method(), newFormBody.build());
            }
            Request request = requestBuilder.build();
            return chain.proceed(request);
        }
    }

}
