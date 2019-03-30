package com.dkaishu.tcraft.feature.sample;

import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ShellUtils;
import com.dkaishu.tcraft.R;
import com.dkaishu.tcraftlib.base.BaseActivity;

/**
 * Created by dks on 2019/3/30 0030.
 */
public class CmdActivity extends BaseActivity {
    @Override
    protected int setLayoutRes() {
        return R.layout.activity_cmd;
    }

    @Override
    protected void initView() {
        TextView tvResult = findViewById(R.id.tv_result);
        findViewById(R.id.bt_exec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShellUtils.CommandResult result =  ShellUtils.execCmd("adb version",true);//adb version
                LogUtils.e(result.toString());
                tvResult.setText(result.toString());
            }
        });

    }

    @Override
    protected void initData() {

    }
}
