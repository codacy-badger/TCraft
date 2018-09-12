package com.tincher.tcraftlib.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.tincher.tcraftlib.R;
import com.tincher.tcraftlib.app.PermissionsChecker;
import com.tincher.tcraftlib.config.PermissionConfig;
import com.tincher.tcraftlib.network.networkstatus.NetInfo;
import com.tincher.tcraftlib.network.networkstatus.NetworkStateListener;
import com.tincher.tcraftlib.network.networkstatus.NetworkStateReceiver;
import com.tincher.tcraftlib.utils.DensityHelper;

import java.util.Arrays;

import static com.tincher.tcraftlib.app.PermissionsChecker.verifyPermissions;

/**
 * Created by dks on 2018/8/3.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";


    protected abstract int initLayout();

    protected abstract void initView();

    protected abstract void initData();

    protected String[] addCheckPermissions() {
        return null;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DensityHelper.setCustomDensity(BaseActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        initView();
        initData();
        initNetworkStateListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNeedCheckPermission) {
            String[] comm = PermissionConfig.permissions;
            String[] add  = addCheckPermissions();
            String[] result;
            if (add != null) {
                result = Arrays.copyOf(comm, comm.length + add.length);
                System.arraycopy(add, 0, result, comm.length, add.length);
            } else {
                result = comm;
            }
            PermissionsChecker.checkPermissions(this, PERMISSION_REQUEST_CODE, result);
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        //移除网络状态监听
        if (null != networkStateListener) {
            NetworkStateReceiver.removeNetworkStateListener(networkStateListener);
            NetworkStateReceiver.unRegisterNetworkStateReceiver(this);
        }
        super.onDestroy();
    }


    /**
     * * ****************************************************************************************
     * 网络状态监听器
     **/
    private NetworkStateListener networkStateListener;

    /**
     * 初始化网络状态监听器
     */
    private void initNetworkStateListener() {
        NetworkStateReceiver.registerNetworkStateReceiver(this);
        networkStateListener = new NetworkStateListener() {
            @Override
            public void onNetworkState(boolean isNetworkAvailable, NetInfo netInfo) {
                BaseActivity.this.onNetworkState(isNetworkAvailable, netInfo);
            }
        };
        //添加网络状态监听
        NetworkStateReceiver.addNetworkStateListener(networkStateListener);
    }

    /**
     * 网络状态
     *
     * @param isNetworkAvailable 网络是否可用
     * @param netInfo            网络信息
     */
    protected void onNetworkState(boolean isNetworkAvailable, NetInfo netInfo) {
        //Todo 网络状态
        if (!isNetworkAvailable) {
            Toast.makeText(this, "Network is not available", Toast.LENGTH_LONG).show();
        }
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
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                Log.d(TAG, String.valueOf(paramArrayOfInt));
                showMissingPermissionDialog();
                hasRequest++;
            } else {
                if (null != permissionDialog && permissionDialog.isShowing()) {
                    permissionDialog.dismiss();
                }
                isNeedCheckPermission = false;
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
