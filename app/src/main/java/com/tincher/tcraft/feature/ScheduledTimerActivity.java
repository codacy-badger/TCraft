package com.tincher.tcraft.feature;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tincher.tcraft.R;
import com.tincher.tcraftlib.base.BaseActivity;
import com.tincher.tcraftlib.widget.ScheduledTimer;

import java.util.concurrent.TimeUnit;

/**
 * 计时任务
 * Created by dks on 2018/9/27.
 */

public class ScheduledTimerActivity extends BaseActivity {
    private static final String TAG = "ScheduledTimerActivity";
    private TextView tv_show;
    private Button   bt_start, bt_stop, bt_clean;

    private ScheduledTimer timer;

    private int current = 0;

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_scheduled_timer;
    }

    @Override
    protected void initView() {
        tv_show = findViewById(R.id.tv_show);
        bt_start = findViewById(R.id.bt_start);
        bt_stop = findViewById(R.id.bt_stop);
        bt_clean = findViewById(R.id.bt_clean);

        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer = new ScheduledTimer(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                current++;
                                tv_show.setText(String.valueOf(current));
                                timer.schedule();
                                Log.e(TAG, String.valueOf(current));
                            }
                        });

                    }
                }, 1, TimeUnit.SECONDS);
                timer.schedule();

            }
        });

        bt_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != timer) timer.shutdown();

            }
        });

        bt_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = 0;

            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != timer) timer.shutdown();

    }
}
