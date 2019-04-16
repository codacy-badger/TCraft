package com.dkaishu.tcraftlib.network;

import com.dkaishu.tcraftlib.app.AppContext;
import com.dkaishu.tcraftlib.app.TLibManager;
import com.dkaishu.tcraftlib.config.NetConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dks on 2018/8/3.
 */

public class RetrofitClient {
    private static RetrofitClient INSTANCE = null;

    private RetrofitClient() {
    }

    public static RetrofitClient getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new RetrofitClient();
        }
        return INSTANCE;
    }

    private Retrofit mRetrofit;
    private String mBaseUrl = TLibManager.getBaseUrl();

    private String mLastToken = "";

    private boolean baseUrlChanged     = false;
    private boolean interceptorsUpdate = false;

    private final List<Interceptor> interceptors = new ArrayList<>();


    public <T> T createService(Class<T> serviceClass) {
        return createService(serviceClass, null);
    }


    public <T> T createService(final Class<T> serviceClass, AccessToken accessToken) {
        String currentToken = accessToken == null ? "" : accessToken.getAccessToken();

        if (null == mRetrofit || !mLastToken.equals(currentToken) || baseUrlChanged || interceptorsUpdate) {

            mLastToken = currentToken;

            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            //
//            if (BuildConfig.DEBUG) {
//                okHttpClientBuilder.addInterceptor(new ChuckInterceptor(AppContext.getsAppContext()));

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
//            }

            if (!interceptors.isEmpty()) {
                for (Interceptor interceptor : interceptors) {
                    okHttpClientBuilder.addInterceptor(interceptor);
                }
            }

            okHttpClientBuilder
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request         original       = chain.request();
                            Request.Builder requestBuilder = original.newBuilder();
                            requestBuilder.header("token", mLastToken)
                                    .method(original.method(), original.body());

                            return chain.proceed(requestBuilder.build());
                        }
                    })
                    .connectTimeout(NetConfig.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(NetConfig.READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(NetConfig.WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(NetConfig.RETRY_ON_CONNECTION_FAILURE)
                    .cache(new Cache(AppContext.getsAppContext().getCacheDir(), NetConfig.CACHE_SIZE * 1024 * 1024));

            Retrofit.Builder builder = new Retrofit.Builder();
            builder
                    .baseUrl(mBaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
            mRetrofit = builder
                    .client(okHttpClientBuilder.build())
                    .build();

            baseUrlChanged = false;
            interceptorsUpdate = false;

        }

        return mRetrofit.create(serviceClass);

    }


    public RetrofitClient notifyBaseUrlchanged() {
        baseUrlChanged = true;
        return getInstance();
    }

    public RetrofitClient addInterceptor(Interceptor interceptor) {
        if (interceptor != null) interceptors.add(interceptor);
        interceptorsUpdate = true;
        return getInstance();
    }
}
