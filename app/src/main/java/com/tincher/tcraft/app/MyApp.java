package com.tincher.tcraft.app;

import com.tincher.tcraftlib.app.TCraft;
import com.tincher.tcraftlib.app.TLibManager;

/**
 * Created by dks on 2018/9/5.
 */

public class MyApp extends TCraft {
    @Override
    public void onCreate() {
        super.onCreate();
        TLibManager.init(new TLibManager.TBuilder().baseUrl("dkaishu.com"));
    }
}
