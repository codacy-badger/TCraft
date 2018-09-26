package com.tincher.tcraftlib.app;

import android.annotation.SuppressLint;
import android.app.Application;

/**
 * 全局变量容器
 * Created by dks on 2018/8/3.
 */

public class AppContext extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Application mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = this;
    }

    public static Application getmAppContext() {
        return mAppContext;
    }
}
