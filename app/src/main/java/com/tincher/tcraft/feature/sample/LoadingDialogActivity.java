package com.tincher.tcraft.feature.sample;

import android.widget.Button;

import com.blankj.utilcode.util.ToastUtils;
import com.tincher.tcraft.R;
import com.tincher.tcraftlib.base.BaseHttpActivity;
import com.tincher.tcraftlib.network.networkstatus.NetInfo;

/**
 * Created by dks on 2018/12/26.
 */
public class LoadingDialogActivity extends BaseHttpActivity {
    @Override
    protected void onNetworkStateChanged(boolean isNetworkAvailable, NetInfo netInfo) {
        ToastUtils.showShort(isNetworkAvailable ? "已联网" : "网络已断开");
    }

    private Button bt_show, bt_show_success, bt_show_failed, bt_set_text;

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_loading_dialog;
    }

    @Override
    protected void initView() {

        bt_show = findViewById(R.id.bt_show);
        bt_show_success = findViewById(R.id.bt_show_success);
        bt_show_failed = findViewById(R.id.bt_show_failed);
        bt_set_text = findViewById(R.id.bt_set_text);

        bt_show.setOnClickListener(v -> {
            showLoadingDialog("666");
        });

        bt_show_success.setOnClickListener(v -> {
            showLoadingSucceed();
        });

        bt_show_failed.setOnClickListener(v -> {
            showLoadingFailed();
        });



        bt_set_text.setOnClickListener(v -> {
            showLoadingDialog();
            //默认800毫秒延迟显示dialog，显示以后才能更改文字显示
            // 如果想直接显示文字已改使用 showLoadingDialog("文字")
            bt_set_text.postDelayed(() -> {
                runOnUiThread(() -> {
                    setLoadingText("123456");
                });
            },2000);
        });

        //更改延迟显示时间
        setDelayMillis(100);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDialogDismissed() {
        ToastUtils.showShort("dismiss");
    }

    @Override
    public void onBackPressed() {
        if (getLoadingDialog().isShowing()) {
            dismissLoadingDialog();
        } else
            super.onBackPressed();
    }
}
