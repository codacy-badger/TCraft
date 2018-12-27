package com.tincher.tcraftlib.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * 今日头条团队适配方案
 * Created by dks on 2018/9/12.
 */

public class DensityHelper {
    private static float coefficient = 360;//例：设计图 1920px * 1080px ，约定 density 为3，即 640dp * 360dp 为标准 ，以宽度来适配，因此coefficient = 360
    private static float          appDensity;
    private static float          appScaledDensity;
    private static DisplayMetrics appDisplayMetrics;

    private DensityHelper() {
    }

    public static void setCustomDensity(@NonNull final Application application) {
        appDisplayMetrics = application.getResources().getDisplayMetrics();
        if (appDensity == 0) {
            appDensity = appDisplayMetrics.density;
            appScaledDensity = appDisplayMetrics.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {

                    if (newConfig != null && newConfig.fontScale > 0) {
                        appScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }
    }

    public static void setCustomDensity(@NonNull final Activity activity) {

        if (appDensity == 0) return;

        final float targetDensity       = appDisplayMetrics.widthPixels / coefficient;
        final float targetScaledDensity = targetDensity * (appScaledDensity / appDensity);
        final int   targetDensityDpi    = (int) (160 * targetDensity);

        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;

    }


}
