package com.dkaishu.tcraftlib.app;

import android.app.ActivityManager;
import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.dkaishu.tcraftlib.BuildConfig;
import com.dkaishu.tcraftlib.location.LocationHelper;
import com.dkaishu.tcraftlib.utils.DensityHelper;

/**
 * 主module需要继承的的Application
 * Created by dks on 2018/9/5.
 */

public class TCraft extends AppContext {
    @Override
    public void onCreate() {
        super.onCreate();

        //防止多进程多次创建
        if (TCraftManager.isMainProcess(this)) initTCraft();

    }

    private void initTCraft() {

//        LeakCanary.install(this);

        //屏幕适配
        DensityHelper.setCustomDensity(getsAppContext());

        Utils.init(this);
        LogUtils.getConfig()
                .setLogSwitch(BuildConfig.DEBUG)
                .setLog2FileSwitch(false)
                .setDir(TLibManager.getLogFilePath())
                .setConsoleSwitch(BuildConfig.DEBUG);

        LocationHelper.init(this);
    }


   public static class TCraftManager {

       public  static boolean isMainProcess(Context context) {
            return context.getPackageName().equals(getCurrentProcessName(context));
        }


       public static String getCurrentProcessName(Context context) {
            int             pid         = android.os.Process.myPid();
            String          processName = "";
            ActivityManager manager     = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (manager != null) {
                for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
                    if (process.pid == pid) {
                        processName = process.processName;
                    }
                }
            }
            return processName;
        }
    }

}
