package com.cheikh.lazywaimai.network;

import android.content.Context;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import com.cheikh.lazywaimai.context.AppConfig;
import com.cheikh.lazywaimai.network.service.AccountService;
import com.cheikh.lazywaimai.network.service.AddressService;
import com.cheikh.lazywaimai.network.service.BusinessService;
import com.cheikh.lazywaimai.network.service.CommonService;
import com.cheikh.lazywaimai.network.service.OrderService;
import com.cheikh.lazywaimai.network.service.TokenService;
import com.cheikh.lazywaimai.util.Constants.Header;
import com.cheikh.lazywaimai.util.SystemUtil;

public class RestApiClient {

    private static final String DEVICE_TYPE = "android";

    private Context mContext;
    private final File mCacheLocation;
    private Retrofit mRetrofit;
    private String mToken;

    public RestApiClient(Context context, File cacheLocation) {
        mContext = context;
        mCacheLocation = cacheLocation;
    }

    public RestApiClient setToken(String token) {
        mToken = token;
        mRetrofit = null;
        return this;
    }

    private OkHttpClient newRetrofitClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 设置缓存路径
        if (mCacheLocation != null) {
            File cacheDir = new File(mCacheLocation, UUID.randomUUID().toString());
            Cache cache = new Cache(cacheDir, 1024);
            builder.cache(cache);
        }
        // 设置超时时间
        builder.connectTimeout(AppConfig.CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        builder.readTimeout(AppConfig.READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        // 添加请求签名拦截器
        builder.addInterceptor(new RequestSignInterceptor());
        // 添加请求头
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder newRequest = chain.request().newBuilder();
                newRequest.addHeader(Header.HTTP_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
                newRequest.addHeader(Header.HTTP_APP_VERSION, SystemUtil.getAppVersionName(mContext));
                newRequest.addHeader(Header.HTTP_APP_KEY, AppConfig.APP_KEY);
                newRequest.addHeader(Header.HTTP_DEVICE_ID, SystemUtil.getDeviceId(mContext));
                newRequest.addHeader(Header.HTTP_DEVICE_TYPE, DEVICE_TYPE);
                if (mToken != null) {
                    newRequest.addHeader(Header.AUTHORIZATION, "Bearer " + mToken);
                }

                return chain.proceed(newRequest.build());
            }
        });
        // 添加OkHttp日志打印器
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(AppConfig.DEBUG ? Level.BODY : Level.NONE);
        builder.addInterceptor(logging);

        return builder.build();
    }

    private Retrofit getRetrofit() {
        if (mRetrofit == null) {
            Retrofit.Builder builder = new Retrofit.Builder();
            builder.client(newRetrofitClient());
            builder.baseUrl(AppConfig.SERVER_URL);
            builder.addConverterFactory(GsonConverterFactory.create(GsonHelper.builderGson()));
            builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());

            mRetrofit = builder.build();
        }

        return mRetrofit;
    }

    private <T> T get(Class<T> clazz) {
        return getRetrofit().create(clazz);
    }

    @SuppressWarnings("unchecked")
    private <T> T getByProxy(Class<T> clazz) {
        T t = get(clazz);
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] { clazz },
                new ResponseErrorProxy(t, this));
    }

    public TokenService tokenService() {
        return getByProxy(TokenService.class);
    }

    public AccountService accountService() {
        return getByProxy(AccountService.class);
    }

    public AddressService addressService() {
        return getByProxy(AddressService.class);
    }

    public BusinessService businessService() {
        return getByProxy(BusinessService.class);
    }

    public OrderService orderService() {
        return getByProxy(OrderService.class);
    }

    public CommonService commonService() {
        return getByProxy(CommonService.class);
    }
}
