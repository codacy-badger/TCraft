package com.tincher.tcraftlib.view;

import android.view.View;

import com.tincher.tcraftlib.utils.ClickTrimHelper;

/**
 * Created by dks on 2018/12/12.
 */
public abstract class OnTrimClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        if (!ClickTrimHelper.isFastClick()) {
            onTrimClick();
        }
    }

    public abstract void onTrimClick();
}
