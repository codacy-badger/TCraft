package com.dkaishu.tcraft.feature.sample;

import android.view.View;
import android.widget.TextView;

import com.dkaishu.tcraft.R;
import com.dkaishu.tcraftlib.base.BaseActivity;
import com.dkaishu.tcraftlib.view.OnTrimClickListener;

/**
 * Created by dks on 2018/12/26.
 */
public class TrimClickActivity extends BaseActivity {
    private TextView tvTimes;
    private  int      time;

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_trim_click;
    }

    @Override
    protected void initView() {
        tvTimes = findViewById(R.id.tv_times);
        findViewById(R.id.bt_no_trim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time += 1;
                update();
            }
        });

        findViewById(R.id.bt_trim).setOnClickListener(new OnTrimClickListener() {
            @Override
            public void onTrimClick() {
                time+=1;
                update();
            }
        });

    }

    @Override
    protected void initData() {
    }

    private void update() {
        tvTimes.setText("" + time);
    }
}
