package com.dkaishu.tcraftlib.app;

import android.annotation.SuppressLint;
import android.app.Application;

/**
 * 全局变量容器
 * Created by dks on 2018/8/3.
 */

public class AppContext extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Application sAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = this;
    }

    public static Application getsAppContext() {
        return sAppContext;
    }
}
