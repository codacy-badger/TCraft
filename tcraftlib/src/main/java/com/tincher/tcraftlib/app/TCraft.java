package com.tincher.tcraftlib.app;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.squareup.leakcanary.LeakCanary;
import com.tincher.tcraftlib.BuildConfig;
import com.tincher.tcraftlib.utils.DensityHelper;

/**
 * 主module需要继承的的Application
 * Created by dks on 2018/9/5.
 */

public class TCraft extends AppContext {
    @Override
    public void onCreate() {
        super.onCreate();
        //屏幕适配
        DensityHelper.setCustomDensity(getmAppContext());
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
        LeakCanary.install(this);

        Utils.init(this);
        LogUtils.getConfig()
                .setLogSwitch(BuildConfig.DEBUG)
                .setLog2FileSwitch(false)
//                .setDir(PathConfig.PATH_LOG_FILE)
                .setConsoleSwitch(BuildConfig.DEBUG);

    }
}
