package com.tincher.tcraftlib.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;

import com.tincher.tcraftlib.network.networkstatus.NetInfo;
import com.tincher.tcraftlib.network.networkstatus.NetworkStateListener;
import com.tincher.tcraftlib.network.networkstatus.NetworkStateReceiver;
import com.tincher.tcraftlib.widget.LoadingDialog;

/**
 * 添加LoadingDialog和网络状态监听
 * Created by dks on 2018/9/5.
 */

public abstract class BaseHttpActivity extends BaseActivity implements BaseHandler.CallBack {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initNetworkStateListener();
    }

    @Override
    protected void onDestroy() {
        //移除网络状态监听
        if (null != networkStateListener) {
            try {
                NetworkStateReceiver.removeNetworkStateListener(networkStateListener);
                NetworkStateReceiver.unRegisterNetworkStateReceiver(BaseHttpActivity.this);
                networkStateListener = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (null != mainHandler) {
            mainHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }


    private BaseHandler   mainHandler;
    private LoadingDialog mLoadingDialog;

    private final static int SHOW_DIALOG    = 0x001;
    private final static int DISMISS_DIALOG = 0x002;
    private final static int LOAD_SUCCEED   = 0x003;
    private final static int LOAD_FAILED    = 0x004;
    private final static int SET_TEXT       = 0x005;

    private final    boolean isSucceedFailedAutoDismiss = true;
    private volatile boolean allowedToShow              = false;


    /**
     * 对于以下公共方法，无需处理线程问题，可直接在子线程调用。
     */
    public void showLoadingDialog() {
        showLoadingDialog("加载中");
    }

    public void showLoadingDialog(String tip) {
        Message msg = new Message();
        msg.what = SHOW_DIALOG;
        msg.obj = tip;
        getMainHandler().sendMessageDelayed(msg, 800);//显示与隐藏之间间隔小于800ms不显示
        allowedToShow = true;
    }

    public void dismissLoadingDialog() {
        Message msg = new Message();
        msg.what = DISMISS_DIALOG;
        getMainHandler().sendMessage(msg);
    }

    public void dismissLoadingDialog(long delayMillis) {
        Message msg = new Message();
        msg.what = DISMISS_DIALOG;
        getMainHandler().sendMessageDelayed(msg, delayMillis);
    }

    public void showLoadingSucceed() {
        Message msg = new Message();
        msg.what = LOAD_SUCCEED;
        getMainHandler().sendMessage(msg);
        if (isSucceedFailedAutoDismiss) {
            dismissLoadingDialog(1600);
        }
    }

    public void showLoadingFailed() {
        Message msg = new Message();
        msg.what = LOAD_FAILED;
        getMainHandler().sendMessage(msg);
        if (isSucceedFailedAutoDismiss) {
            dismissLoadingDialog(1600);
        }
    }

    public void setLoadingText(String text) {
        Message msg = new Message();
        msg.what = SET_TEXT;
        msg.obj = text;
        getMainHandler().sendMessage(msg);
    }

    protected LoadingDialog getLoadingDialog() {
        if (null == mLoadingDialog) {
            mLoadingDialog = new LoadingDialog(BaseHttpActivity.this);
            mLoadingDialog.setCanceledOnTouchOutside(false);
            mLoadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    onDialogDismiss();
                }
            });
        }
        return mLoadingDialog;
    }

    /**
     * 当Dialog dismiss时调动
     * 可Override，以实现对Dialog dismiss的监听
     */
    protected void onDialogDismiss() {

    }

    protected BaseHandler getMainHandler() {
        if (null == mainHandler) {
            mainHandler = new BaseHandler(this, Looper.getMainLooper());
        }
        return mainHandler;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_DIALOG: {
                if (allowedToShow) {
                    getLoadingDialog().showDialog();
                    getLoadingDialog().text((String) msg.obj);
                }
                break;
            }
            case DISMISS_DIALOG: {
                allowedToShow = false;
                getLoadingDialog().dismiss();
                break;
            }
            case LOAD_SUCCEED: {
                getLoadingDialog().succeed();
                break;
            }
            case LOAD_FAILED: {
                getLoadingDialog().failed();
                break;
            }
            case SET_TEXT: {
                getLoadingDialog().text((String) msg.obj);
                break;
            }
        }
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
        NetworkStateReceiver.registerNetworkStateReceiver(BaseHttpActivity.this);
        networkStateListener = new NetworkStateListener() {
            @Override
            public void onNetworkState(boolean isNetworkAvailable, NetInfo netInfo) {
                BaseHttpActivity.this.onNetworkStateChanged(isNetworkAvailable, netInfo);
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
    protected abstract void onNetworkStateChanged(boolean isNetworkAvailable, NetInfo netInfo);


}
