package com.tincher.tcraft.feature.sample;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tincher.tcraft.R;
import com.tincher.tcraft.data.MyApiService;
import com.tincher.tcraft.data.base.BaseHttpObserver;
import com.tincher.tcraft.data.model.Categories;
import com.tincher.tcraftlib.base.BaseHttpActivity;
import com.tincher.tcraftlib.network.RetrofitClient;
import com.tincher.tcraftlib.network.networkstatus.NetInfo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Http 请求示例
 * Created by dks on 2018/9/27.
 */

public class HttpRequestActivity extends BaseHttpActivity {
    private static final String   TAG = "HttpRequestActivity";

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_http_request;
    }

    @Override
    protected void initView() {
        findViewById(R.id.bt_start).setOnClickListener(v -> httpRequest());
    }

    @Override
    protected void initData() {
    }

    private void httpRequest() {
        RetrofitClient.getInstance().createService(MyApiService.class)
                .getXD()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .doOnSubscribe(disposable -> showLoadingDialog())
                .doFinally(this::dismissLoadingDialog)
                .subscribe(new BaseHttpObserver<Categories>() {
                    @Override
                    public void onSuccess(Categories categories) {
                        Log.d(TAG, "onSuccess: "+String.valueOf(categories.getResults().size()));
                    }
                    //可override其他方法
                });
    }
}
