package com.tincher.tcraftlib.utils;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.NotificationCompat;

import com.tincher.tcraftlib.BuildConfig;
import com.tincher.tcraftlib.app.AppContext;

/**
 * 通知
 * Created by dks on 2018/11/30.
 */

public class NotificationHelper {

    private final static String CHANNEL_ID_DEFAULT = BuildConfig.mApplicationName;

    private static NotificationManager notificationManager;

    private NotificationHelper() {
    }

    /**
     * 通知栏显示一条通知
     */
    public static synchronized void showNotification(Intent intent, int icon, String title, String content
            , long timeMillis, int id) {
        showNotification(intent, icon, title, content, timeMillis, id, CHANNEL_ID_DEFAULT);
    }

    public static synchronized void showNotification(Intent intent, int icon, String title
            , String content, long timeMillis, int id, String channelId) {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) AppContext.getmAppContext()
                    .getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                    new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_LOW));
        }

        show(intent, icon, title, content, timeMillis, id, null, channelId);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static synchronized void showNotification(Intent intent, int icon, String title, String content
            , long timeMillis, int id, NotificationChannel channel) {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) AppContext.getmAppContext()
                    .getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && channel != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        show(intent, icon, title, content, timeMillis, id, channel, "");
    }

    private static void show(Intent intent, int icon, String title
            , String content, long timeMillis, int id, NotificationChannel channel, String channelId) {

        String ci;
        if (channel == null) {
            ci = channelId;
        } else {
            ci = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? channel.getId() : "";
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(AppContext
                .getmAppContext(), ci)
                .setContentIntent(PendingIntent.getActivity(AppContext.getmAppContext()
                        , 0, intent, 0))
                .setSmallIcon(icon)
                .setChannelId(ci)
//                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setWhen(timeMillis)
                .setTicker(title)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

//        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        notificationManager.notify(id, builder.build());

    }


    /**
     * 启动振动
     */
    @RequiresPermission(android.Manifest.permission.VIBRATE)
    public static void vibrate(long milliseconds) {
        Vibrator vib = (Vibrator) AppContext.getmAppContext().getSystemService(Service.VIBRATOR_SERVICE);
        if (vib != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                VibrationEffect vibrationEffect = VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE);
                vib.vibrate(vibrationEffect);
            } else {
                vib.vibrate(milliseconds);
            }
        }
    }

    /**
     * @param amplitude 震动强度 This must be a value between 1 and 255，or VibrationEffect.DEFAULT_AMPLITUDE
     */
    @RequiresPermission(android.Manifest.permission.VIBRATE)
    public static void vibrate(long milliseconds, int amplitude) {
        Vibrator vib = (Vibrator) AppContext.getmAppContext().getSystemService(Service.VIBRATOR_SERVICE);
        if (vib != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                VibrationEffect vibrationEffect = VibrationEffect.createOneShot(milliseconds, amplitude);
                vib.vibrate(vibrationEffect);
            } else {
                vib.vibrate(milliseconds);
            }
        }
    }


    @RequiresPermission(android.Manifest.permission.VIBRATE)
    public static void vibrate(long[] pattern, int repeat) {
        Vibrator vib = (Vibrator) AppContext.getmAppContext().getSystemService(Service.VIBRATOR_SERVICE);
        if (vib != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                VibrationEffect vibrationEffect = VibrationEffect.createWaveform(pattern, repeat);
                vib.vibrate(vibrationEffect);
            } else {
                vib.vibrate(pattern, repeat);
            }
        }
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    public static void vibrate(long[] pattern, int repeat, AudioAttributes attributes) {
        Vibrator vib = (Vibrator) AppContext.getmAppContext().getSystemService(Service.VIBRATOR_SERVICE);
        if (vib != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                VibrationEffect vibrationEffect = VibrationEffect.createWaveform(pattern, repeat);
                vib.vibrate(vibrationEffect, attributes);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    vib.vibrate(pattern, repeat, attributes);
                } else {
                    vib.vibrate(pattern, repeat);
                }
            }
        }
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    public static void vibrateCancel() {
        Vibrator vib = (Vibrator) AppContext.getmAppContext().getSystemService(Service.VIBRATOR_SERVICE);
        if (vib != null) {
            vib.cancel();
        }
    }


    private static MediaPlayer sMediaPlayer;

    /**
     * MediaPlayer 播放手机铃声
     */
    public static void playRing() {
        if (sMediaPlayer == null) {
            sMediaPlayer = new MediaPlayer();
        }
        if (sMediaPlayer.isPlaying()) {
            return;
        }
        try {
            Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);//用于获取手机默认铃声的Uri
            sMediaPlayer.setDataSource(AppContext.getmAppContext(), alert);
            sMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);//铃声流
            sMediaPlayer.setLooping(true);
            sMediaPlayer.prepare();
            sMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止播放铃声
     */
    public static void stopRing() {
        if (sMediaPlayer != null) {
            if (sMediaPlayer.isPlaying()) {
                sMediaPlayer.stop();
                sMediaPlayer.release();
            }
            sMediaPlayer = null;
        }
    }

}
