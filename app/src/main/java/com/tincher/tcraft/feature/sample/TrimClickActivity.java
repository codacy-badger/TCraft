package com.tincher.tcraft.feature.sample;

import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.ToastUtils;
import com.tincher.tcraft.R;
import com.tincher.tcraftlib.base.BaseActivity;
import com.tincher.tcraftlib.utils.ClickTrimHelper;
import com.tincher.tcraftlib.view.OnTrimClickListener;

/**
 * Created by dks on 2018/12/26.
 */
public class TrimClickActivity extends BaseActivity {

    private Button bt_no_trim, bt_trim;

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_trim_click;
    }

    @Override
    protected void initView() {
        bt_trim = findViewById(R.id.bt_trim);
        bt_no_trim = findViewById(R.id.bt_no_trim);

        bt_no_trim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickTrimHelper.isFastClick())
                    ToastUtils.showShort("太快了");
            }
        });

        bt_trim.setOnClickListener(new OnTrimClickListener() {
            @Override
            public void onTrimClick() {
                ToastUtils.showShort("奏效了");
            }
        });

    }

    @Override
    protected void initData() {

    }
}
