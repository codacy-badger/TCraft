package com.tincher.tcraft.feature;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;

import com.tincher.tcraft.BuildConfig;
import com.tincher.tcraft.R;
import com.tincher.tcraftlib.base.BaseActivity;
import com.tincher.tcraftlib.utils.NotificationHelper;

/**
 * 通知、震动、响铃
 * Created by dks on 2018/11/30.
 */

public class NotificationActivity extends BaseActivity {
    @Override
    protected int setLayoutRes() {
        return R.layout.activity_notification;
    }

    @Override
    protected void initView() {
        Button bt_send_a_noti_1 = findViewById(R.id.bt_send_a_noti_1);
        Button bt_send_a_noti_2 = findViewById(R.id.bt_send_a_noti_2);
        Button bt_vibrate_1     = findViewById(R.id.bt_vibrate_1);
        Button bt_vibrate_2     = findViewById(R.id.bt_vibrate_2);
        Button bt_vibrate_3     = findViewById(R.id.bt_vibrate_3);
        Button bt_vibrate_stop  = findViewById(R.id.bt_vibrate_stop);
        Button bt_ring          = findViewById(R.id.bt_ring);
        Button bt_ring_stop     = findViewById(R.id.bt_ring_stop);

        bt_send_a_noti_1.setText("通知1");
        bt_send_a_noti_2.setText("通知2");
        bt_vibrate_1.setText("震动1");
        bt_vibrate_2.setText("震动2");
        bt_vibrate_3.setText("震动3");
        bt_vibrate_stop.setText("震动取消");
        bt_ring.setText("响铃");
        bt_ring_stop.setText("停止响铃");

        bt_send_a_noti_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationActivity.this, NotificationActivity.class);
                NotificationHelper.showNotification(intent, R.mipmap.ic_launcher, "通知1", "通知1内容", System.currentTimeMillis(), 1200);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        bt_send_a_noti_2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationActivity.this, NotificationActivity.class);
                NotificationChannel channel = new NotificationChannel(BuildConfig.mApplicationName,
                        BuildConfig.mApplicationName
                        , NotificationManager.IMPORTANCE_LOW);
                NotificationHelper.showNotification(intent, R.mipmap.ic_launcher, "通知2", "通知2内容", System.currentTimeMillis(), 1201, channel);
            }
        });

        bt_vibrate_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationHelper.vibrate(2200, 200);
            }
        });

        bt_vibrate_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long[] pattern = new long[]{500, 2000, 500, 2000};
                NotificationHelper.vibrate(pattern, 2);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            bt_vibrate_3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                AudioAttributes.Builder builder = new AudioAttributes.Builder();
                builder.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                        .setLegacyStreamType(AudioManager.STREAM_ALARM)
                        .setUsage(AudioAttributes.USAGE_ALARM);
                AudioAttributes attributes = builder.build();
                long[]          pattern    = new long[]{500, 2000, 500, 2000};
                NotificationHelper.vibrate(pattern, 1, attributes);
            }
        });

        bt_vibrate_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationHelper.vibrateCancel();
            }
        });

        bt_ring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationHelper.playRing();
            }
        });
        bt_ring_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationHelper.stopRing();
            }
        });


    }

    @Override
    protected void initData() {

    }
}
