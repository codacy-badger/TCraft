package com.dkaishu.tcraft.feature.sample;

import android.content.pm.PackageManager;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.CloseUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.dkaishu.tcraft.R;
import com.dkaishu.tcraftlib.base.BaseActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import okhttp3.ResponseBody;

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
//                ShellUtils.CommandResult result =  ShellUtils.execCmd("adb version",true);//adb version
//                LogUtils.e(result.toString());
//                tvResult.setText(result.toString());


//                List<AppUtils.AppInfo> mApps = AppUtils.getAppsInfo();
//                for (int i = 0; i < mApps.size(); i++) {
//                    LogUtils.e(mApps.get(i).getPackageName());
//                    String pkgName = mApps.get(i).getPackageName();
//                    if (pkgName.equals("com.google.android.apps.youtube.music")) {
//                        try {
//                            File file = new File(getPackageManager().getApplicationInfo(pkgName, 0).sourceDir);
//                            tvResult.setText(file.getName());
//                            boolean succ = FileUtils.copyFile(file.getPath(), Environment.getExternalStorageDirectory() + "/" + pkgName + ".apk");
//                            LogUtils.e(succ);
//
//                        } catch (PackageManager.NameNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    public File saveFile(ResponseBody responseBody, String destFileDir, String destFileName) {
        String downloadFilePath = destFileDir + File.separator + destFileName;
        if (destFileDir.endsWith(File.separator)) {
            downloadFilePath = destFileDir + destFileName;
        }

        InputStream is = responseBody.byteStream();

        File file = FileUtils.getFileByPath(downloadFilePath);
        if (!FileUtils.createOrExistsFile(file) || is == null) return null;
        OutputStream os = null;

        long total    = responseBody.contentLength();
        int  finalSum = 0;
        int  progress = 0;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file, false));
            byte[] data = new byte[1024];
            int    len  = is.read(data, 0, 1024);
            while (len != -1) {
                os.write(data, 0, len);
                finalSum += len;
                int currentProgress = (int) (finalSum * 100 / total);
                if (progress != currentProgress) {
                    progress = currentProgress;
//                    onSaveProgress(progress, total);
                    LogUtils.e(progress);
                }
                len = is.read(data, 0, 1024);
            }
            return FileUtils.getFileByPath(downloadFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseUtils.closeIO(is, os);
        }
    }
}
