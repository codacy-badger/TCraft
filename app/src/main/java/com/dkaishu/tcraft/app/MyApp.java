package com.dkaishu.tcraft.app;

import com.dkaishu.tcraftlib.app.TCraft;
import com.dkaishu.tcraftlib.app.TLibManager;

/**
 * Created by dks on 2018/9/5.
 */

public class MyApp extends TCraft {
    @Override
    public void onCreate() {
        super.onCreate();
        TLibManager.init(new TLibManager.TBuilder().baseUrl("http://dkaishu.com/"));
    }
}
