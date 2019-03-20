package com.dkaishu.tcraft.data.base;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dkaishu.tcraft.BuildConfig;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.util.EndConsumerHelper;

/**
 * 对请求结果的统一处理
 * Created by dks on 2019/2/26 0026.
 */
public abstract class BaseHttpObserver<T extends BaseResponse> implements Observer<T> {
    final AtomicReference<Disposable> s = new AtomicReference<>();

    public final void cancel() {
        DisposableHelper.dispose(s);
    }

    @Override
    public void onSubscribe(Disposable d) {
        EndConsumerHelper.setOnce(this.s, d, getClass());
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        if (BuildConfig.DEBUG) {
            ToastUtils.showShort(e.getMessage());
            LogUtils.e("onError:" + e.getMessage());
            e.printStackTrace();
        } else {
            ToastUtils.showShort("请求出错");
        }

    }

    @Override
    public void onComplete() {

    }

    public abstract void onSuccess(T t);

}
