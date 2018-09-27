package com.tincher.tcraft.feature;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tincher.tcraft.R;
import com.tincher.tcraftlib.base.BaseHttpActivity;
import com.tincher.tcraftlib.network.NetworkUtil;
import com.tincher.tcraftlib.network.download.DownloadListener;
import com.tincher.tcraftlib.network.download.FileDownLoadObserver;
import com.tincher.tcraftlib.network.networkstatus.NetInfo;

import java.io.File;
import java.text.DecimalFormat;

/**
 * 文件下载
 * Created by dks on 2018/9/27.
 */

public class FileDownloadActivity extends BaseHttpActivity {
    private static final String TAG = "FileDownloadActivity";
    private TextView tv_show;
    private Button   bt_start, bt_stop;

    private final static String downloadUrl
            = "http://ftp-new-apk.pconline.com.cn/417d498f47ad31e3f36e994c47ce20df/pub/download/201808/pconline1534815265261.apk";
    private FileDownLoadObserver<File> observer;

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_file_download;
    }

    @Override
    protected void initView() {
        tv_show = findViewById(R.id.tv_show);
        bt_start = findViewById(R.id.bt_start);
        bt_stop = findViewById(R.id.bt_stop);
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
            }
        });

        bt_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                observer.cancel();
            }
        });

        observer = new FileDownLoadObserver<File>() {

            @Override
            public void onDownLoadSuccess(File file) {
                Log.d(TAG, "onDownLoadSuccess");
                dismissLoadingDialog();
                AppUtils.installApp(file);
            }

            @Override
            public void onDownLoadFail(Throwable throwable) {
                showLoadingFailed();
                Log.e(TAG, "onDownLoadFail:" + throwable.getMessage());
                throwable.printStackTrace();
            }

            @Override
            public void onSaveProgress(int progress, Long total) {
                Log.d(TAG, "onSaveProgress: " + progress);
            }

        };

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onNetworkStateChanged(boolean isNetworkAvailable, NetInfo netInfo) {
        if (!isNetworkAvailable) {
            ToastUtils.showShort("网络不可用");
        }
    }


    private void download() {
        showLoadingDialog();
        NetworkUtil.download(downloadUrl, Environment.getExternalStorageDirectory().getPath()
                , "66.apk", new DownloadListener() {
                    @Override
                    public void onProgress(final float progress, long total) {
                        final DecimalFormat df = new DecimalFormat("0.00");
                        setLoadingText(df.format(progress) + " %");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_show.setText(df.format(progress) + " %");
                            }
                        });
                    }
                }
                , observer
        );
    }

}
