package com.tincher.tcraftlib.network;

import android.annotation.SuppressLint;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tincher.tcraftlib.BuildConfig;
import com.tincher.tcraftlib.app.AppContext;
import com.tincher.tcraftlib.config.NetConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dks on 2018/8/3.
 */

public class RetrofitClient {
    @SuppressLint("StaticFieldLeak")
    private static RetrofitClient INSTANCE = null;

    private RetrofitClient() {
    }

    public static RetrofitClient getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new RetrofitClient();
        }
        return INSTANCE;
    }

/*    public static void init(Application mAppContext, String baseUrl) {
        getInstance().mAppContext = mAppContext;

    }*/


    private Retrofit mRetrofit;
    private String mBaseUrl = "";

    private String mLastToken = "";

    public <T> T createService(Class<T> serviceClass) {
        return createService(serviceClass, null);
    }


    public <T> T createService(final Class<T> serviceClass, AccessToken accessToken) {
        String currentToken = accessToken == null ? "" : accessToken.getAccessToken();

        if (null == mRetrofit || !mLastToken.equals(currentToken)) {
            mLastToken = currentToken;
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            //
            if (BuildConfig.DEBUG) {
                okHttpClientBuilder.addInterceptor(new ChuckInterceptor(AppContext.getmAppContext()));

/*                okHttpClientBuilder.addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        return null;
                    }
                });
                okHttpClientBuilder.authenticator(new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        return null;
                    }
                });*/
            }

            okHttpClientBuilder
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request         original       = chain.request();
                            Request.Builder requestBuilder = original.newBuilder();
                            requestBuilder.header("token", mLastToken)
                                    .method(original.method(), original.body());

                            //todo Response 拦截监听

                            return chain.proceed(requestBuilder.build());
                        }
                    })
                    .connectTimeout(NetConfig.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(NetConfig.READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(NetConfig.WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(NetConfig.RETRY_ON_CONNECTION_FAILURE)
                    .cache(new Cache(AppContext.getmAppContext().getCacheDir(), NetConfig.CACHE_SIZE * 1024 * 1024));

            Retrofit.Builder builder = new Retrofit.Builder();
            builder
                    .baseUrl(mBaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
            mRetrofit = builder
                    .client(okHttpClientBuilder.build())
                    .build();

        }

        return mRetrofit.create(serviceClass);

    }


}
