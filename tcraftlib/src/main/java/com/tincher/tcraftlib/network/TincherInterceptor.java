package com.tincher.tcraftlib.network;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Handshake;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * TincherInterceptor 不合理 ，-->请求结果拦截器
 * Created by dks on 2018/9/5.
 */

public final class TincherInterceptor implements Interceptor {
    private static final String TAG = "TincherInterceptor";
    private String                     mAccessToken;
    private TincherInterceptorCallback interceptorCallback;


    public TincherInterceptor(String accessToken) {
        this.mAccessToken = accessToken;
    }

    public TincherInterceptorCallback getInterceptorCallback() {
        return interceptorCallback;
    }

    public void setInterceptorCallback(TincherInterceptorCallback interceptorCallback) {
        this.interceptorCallback = interceptorCallback;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request         original       = chain.request();
        Request.Builder requestBuilder = original.newBuilder();
        requestBuilder.header("token", mAccessToken)
                .method(original.method(), original.body());

        long     startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(requestBuilder.build());
            Log.e(TAG, "chain.proceed ------");
        } catch (Exception e) {
//            transaction.setError(e.toString());
//            update(transaction, transactionUri);
            Log.e(TAG, "chain.proceed Exception");

            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        Log.d(TAG, "proceed time: " + String.valueOf(tookMs));



        ResponseBody responseBody    = response.body();
        ResponseBody newResponseBody = null;
        if (interceptorCallback != null) {

            newResponseBody = interceptorCallback.OnResponse(responseBody);
        } else {
            newResponseBody = responseBody;
        }
        if (newResponseBody == null) {
            newResponseBody = responseBody;
        }

        Headers   responseHeaders                  = response.headers();
        Protocol  responseprotocol                 = response.protocol();
        int       responsecode                     = response.code();
        String    responsemessage                  = response.message();
        Handshake responsehandshake                = response.handshake();
        Response  responsenetworkResponse          = response.networkResponse();
        Response  responsecacheResponse            = response.cacheResponse();
        Response  responsepriorResponse            = response.priorResponse();
        long      responsesentRequestAtMillis      = response.sentRequestAtMillis();
        long      responsereceivedResponseAtMillis = response.receivedResponseAtMillis();


        Response.Builder responseBuilder = response.newBuilder();
        Response newResponse = responseBuilder.body(newResponseBody)
                .headers(responseHeaders)
                .protocol(responseprotocol)
                .code(responsecode)
                .message(responsemessage)
                .handshake(responsehandshake)
                .networkResponse(responsenetworkResponse)
                .cacheResponse(responsecacheResponse)
                .priorResponse(responsepriorResponse)
                .sentRequestAtMillis(responsesentRequestAtMillis)
                .receivedResponseAtMillis(responsereceivedResponseAtMillis)
                .build();

        return newResponse;
    }

}
