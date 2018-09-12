package com.tincher.tcraftlib.app;

import android.annotation.SuppressLint;
import android.app.Application;

import com.tincher.tcraftlib.utils.DensityHelper;

/**
 * Created by dks on 2018/8/3.
 */

 public class AppContext extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Application mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = this;
        DensityHelper.setCustomDensity(mAppContext);
    }

    public static Application getmAppContext() {
        return mAppContext;
    }
}
