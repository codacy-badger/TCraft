package com.tincher.tcraftlib.network.download;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tincher.tcraftlib.BuildConfig;
import com.tincher.tcraftlib.app.AppContext;
import com.tincher.tcraftlib.app.TLibManager;
import com.tincher.tcraftlib.config.NetConfig;
import com.tincher.tcraftlib.network.AccessToken;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by dks on 2018/8/3.
 */

public class DownloadRetrofitClient {

    private DownloadRetrofitClient() {
    }

    public static DownloadRetrofitClient newInstance() {
        return new DownloadRetrofitClient();
    }

    private String mBaseUrl = TLibManager.getBaseUrl();


    public <T> T createService(final Class<T> serviceClass, AccessToken accessToken, DownloadListener downloadListener) {
        final String mLastToken = accessToken == null ? "" : accessToken.getAccessToken();


        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            okHttpClientBuilder.addInterceptor(new ChuckInterceptor(AppContext.getsAppContext()));
        }

        if (downloadListener != null) {
            okHttpClientBuilder.addInterceptor(new DownloadInterceptor(downloadListener));
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
                .retryOnConnectionFailure(NetConfig.RETRY_ON_CONNECTION_FAILURE);
//                    .cache(new Cache(AppContext.getsAppContext().getCacheDir(), NetConfig.CACHE_SIZE * 1024 * 1024));

        Retrofit.Builder builder = new Retrofit.Builder();
        builder
                .baseUrl(mBaseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        return builder.client(okHttpClientBuilder.build()).build()
                .create(serviceClass);

    }


}
