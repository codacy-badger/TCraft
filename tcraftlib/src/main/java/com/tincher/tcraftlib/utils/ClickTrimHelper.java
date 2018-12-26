package com.tincher.tcraftlib.utils;

/**
 * Created by dks on 2018/12/12.
 */
public class ClickTrimHelper {

    private static final int MIN_DELAY_TIME= 1000;  // 两次点击最小间隔1000ms
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }


}
