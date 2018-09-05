package com.tincher.tcraftlib.network;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by dks on 2018/9/5.
 */

public final class TincherInterceptor implements Interceptor {
    private String mAccessToken;

    public TincherInterceptor(String accessToken) {
        this.mAccessToken = accessToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request         original       = chain.request();
        Request.Builder requestBuilder = original.newBuilder();
        requestBuilder.header("token", mAccessToken)
                .method(original.method(), original.body());

        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(requestBuilder.build());
        } catch (Exception e) {
//            transaction.setError(e.toString());
//            update(transaction, transactionUri);
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();

        //todo Response 拦截监听



        Response.Builder responseBuilder = response.newBuilder();
        responseBuilder.body(responseBody);

        return responseBuilder.build();
    }
}
