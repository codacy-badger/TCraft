package com.dkaishu.tcraft.app;

import com.dkaishu.tcraftlib.BuildConfig;
import com.dkaishu.tcraftlib.app.TCraft;
import com.dkaishu.tcraftlib.app.TLibManager;
import com.dkaishu.tcraftlib.network.RetrofitClient;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by dks on 2018/9/5.
 */

public class MyApp extends TCraft {
    @Override
    public void onCreate() {
        super.onCreate();
        TLibManager.init(new TLibManager.TBuilder().baseUrl("http://dkaishu.com/"));
        if (TCraftManager.isMainProcess(this)){
            initMyApp();
        }
    }

    private void initMyApp(){
        if (BuildConfig.DEBUG){
            RetrofitClient.getInstance().addInterceptor(new ChuckInterceptor(this));
        }
        LeakCanary.install(this);
    }
}
