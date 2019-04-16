package com.dkaishu.tcraft.feature.sample;

import android.util.Log;

import com.dkaishu.tcraft.R;
import com.dkaishu.tcraft.data.MyApiService;
import com.dkaishu.tcraft.data.base.BaseHttpObserver;
import com.dkaishu.tcraft.data.model.Categories;
import com.dkaishu.tcraftlib.base.BaseHttpActivity;
import com.dkaishu.tcraftlib.network.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
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
                .doOnSubscribe(disposable ->{
                            showLoadingDialog();
                            getLoadingDialog().setCancelable(true);
                }
                )
                .doFinally(this::dismissLoadingDialog)
                .subscribe(new BaseHttpObserver<Categories>() {
                    @Override
                    public void onSuccess(Categories categories) {
                        Log.d(TAG, "onSuccess: "+String.valueOf(categories.getResults().size()));
                    }
                    //可override其他方法

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getLoadingDialog().setCancelable(true);
                    }
                });
    }
}
