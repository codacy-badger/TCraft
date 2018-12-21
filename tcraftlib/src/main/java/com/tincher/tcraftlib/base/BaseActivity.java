package com.tincher.tcraftlib.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.tincher.tcraftlib.R;
import com.tincher.tcraftlib.config.PermissionConfig;
import com.tincher.tcraftlib.utils.DensityHelper;
import com.tincher.tcraftlib.utils.PermissionsChecker;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import static com.tincher.tcraftlib.utils.PermissionsChecker.verifyPermissions;

/**
 * Created by dks on 2018/8/3.
 */

public abstract class BaseActivity extends RxAppCompatActivity {
    private static final String TAG = "BaseActivity";

    protected abstract int setLayoutRes();

    protected abstract void initView();

    protected abstract void initData();

    /**
     * 添加需要检测的权限
     */
    protected String[] addCheckPermissions() {
        return null;
    }

    /**
     * 当权限被用户手动允许时调用
     */
    protected void onAllPermissionsGrantedByUser() {
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DensityHelper.setCustomDensity(BaseActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(setLayoutRes());
        if (isNeedCheckPermission) {
            PermissionsChecker.checkPermissions(this, PERMISSION_REQUEST_CODE, PermissionConfig.permissions);
            String[] add = addCheckPermissions();
            if (add != null) {
                PermissionsChecker.checkPermissions(this, PERMISSION_REQUEST_CODE, add);
            }
        }

        initView();
        initData();
    }

    /**
     * ***************************************************************************************
     * 权限检测
     */

    private static final int PERMISSION_REQUEST_CODE = 0;
    private              int hasRequest              = 0;

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheckPermission = true;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] paramArrayOfInt) {
        super.onRequestPermissionsResult(requestCode, permissions, paramArrayOfInt);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog();
                hasRequest++;
            } else {
                if (null != permissionDialog && permissionDialog.isShowing()) {
                    permissionDialog.dismiss();
                }
                isNeedCheckPermission = false;
                onAllPermissionsGrantedByUser();
            }
            if (hasRequest >= 3) {
                showMissingPermissionDialog();
                isNeedCheckPermission = false;
            }
        }
    }

    /**
     * 显示提示信息
     */
    private AlertDialog.Builder builder;
    private AlertDialog         permissionDialog;

    private void showMissingPermissionDialog() {

        if (null == builder) {
            builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.dialog_permission_ok_notifyTitle)
                    .setMessage(R.string.dialog_permission_ok_notifyMsg)
                    .setNegativeButton(R.string.dialog_permission_ok_cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                    .setPositiveButton(R.string.dialog_permission_ok_setting,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startAppSettings();
                                    isNeedCheckPermission = false;
                                }
                            })
                    .setCancelable(false);
            permissionDialog = builder.create();
        } else if (permissionDialog.isShowing()) {
            return;
        }
        permissionDialog.show();
    }

    /**
     * 启动应用的设置
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);

    }


}
