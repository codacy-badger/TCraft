package com.tincher.tcraft.feature.main;

import android.util.Log;

import com.tincher.tcraft.R;
import com.tincher.tcraft.data.MyService;
import com.tincher.tcraftlib.base.BaseHttpActivity;
import com.tincher.tcraftlib.network.RetrofitClient;
import com.tincher.tcraftlib.network.TincherInterceptorCallback;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

public class MainActivity extends BaseHttpActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        RetrofitClient.getInstance().createService(MyService.TestService.class, null, new TincherInterceptorCallback() {
            @Override
            public ResponseBody OnResponse(ResponseBody responseBody) {
                byte[]       bytes;
                MediaType    mediaType;
                ResponseBody newRb = null;
                try {
                    bytes = responseBody.bytes();
                    mediaType = responseBody.contentType();
                    newRb = ResponseBody.create(mediaType, bytes);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "IOException： ");

                }
                Log.e(TAG, "length： ");

                return newRb;
            }
        })
                .getTopMovie()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(String movie) {
                        Log.d(TAG, "onNext: " + movie);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Over!");
                    }
                });

    }


}
