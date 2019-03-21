package com.dkaishu.tcraftlib.view;

import android.view.View;

/**
 * Created by dks on 2018/12/12.
 */
public abstract class OnTrimClickListener implements View.OnClickListener {
    private static final int  MIN_DELAY_TIME = 800;  // 两次点击最小间隔800ms
    private              long lastClickTime;

    @Override
    public void onClick(View v) {
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            lastClickTime = currentClickTime;
            onTrimClick();
        }
    }

    public abstract void onTrimClick();
}
