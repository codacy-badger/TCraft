package com.tincher.tcraft.feature;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tincher.tcraft.R;
import com.tincher.tcraft.data.MyApiService;
import com.tincher.tcraft.data.model.Categories;
import com.tincher.tcraftlib.base.BaseHttpActivity;
import com.tincher.tcraftlib.network.RetrofitClient;
import com.tincher.tcraftlib.network.networkstatus.NetInfo;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Http 请求示例
 * Created by dks on 2018/9/27.
 */

public class HttpRequestActivity extends BaseHttpActivity {
    private static final String TAG = "HttpRequestActivity";
    private TextView tv_show;
    private Button   bt_start, bt_stop;

    private Disposable disposable;


    @Override
    protected int setLayoutRes() {
        return R.layout.activity_http_request;
    }

    @Override
    protected void initView() {
        tv_show = findViewById(R.id.tv_show);
        bt_start = findViewById(R.id.bt_start);
        bt_stop = findViewById(R.id.bt_stop);
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpRequest();
            }
        });

        bt_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (disposable != null) {
                    disposable.dispose();
                    Log.d(TAG, " disposable.dispose ");

                }
            }
        });
    }

    @Override
    protected void initData() {


    }

    @Override
    protected void onNetworkStateChanged(boolean isNetworkAvailable, NetInfo netInfo) {

    }

    private void httpRequest() {
        RetrofitClient.getInstance().createService(MyApiService.xianduService.class)
                .getXD()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Categories>bindToLifecycle())
                .subscribe(new Observer<Categories>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                        disposable = d;
                    }

                    @Override
                    public void onNext(Categories categories) {
                        Log.d(TAG, "onNext: ");
                        tv_show.setText(String.valueOf(categories.getResults().size()));
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
