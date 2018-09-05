package com.tincher.tcraftlib.base;

import android.os.Looper;
import android.os.Message;

import com.tincher.tcraftlib.widget.LoadingDialog;

/**
 * todo wangluo qingqiu qidong quxiao
 * Created by dks on 2018/9/5.
 */

public abstract class BaseHttpActivity extends BaseActivity implements BaseHandler.CallBack {
    private BaseHandler   mainHandler;
    private LoadingDialog mLoadingDialog;

    private final static int SHOW_DIALOG    = 0x001;
    private final static int DISMISS_DIALOG = 0x002;
    private final static int LOAD_SUCCEED   = 0x003;
    private final static int LOAD_FAILED    = 0x004;


    public void showLoadingDialog(){
        Message msg = new Message();
        msg.what = SHOW_DIALOG;
        getMainHandler().sendMessage(msg);
    }
    public void dismissLoadingDialog(){
        Message msg = new Message();
        msg.what = DISMISS_DIALOG;
        getMainHandler().sendMessage(msg);
    }

    public void showLoadingSucceed(){
        Message msg = new Message();
        msg.what = LOAD_SUCCEED;
        getMainHandler().sendMessage(msg);
    }

    public void showLoadingFailed(){
        Message msg = new Message();
        msg.what = LOAD_FAILED;
        getMainHandler().sendMessage(msg);
    }

    protected LoadingDialog getLoadingDialog() {
        if (null == mLoadingDialog) {
            mLoadingDialog = new LoadingDialog(this);
            mLoadingDialog.setCanceledOnTouchOutside(false);
        }
        return mLoadingDialog;
    }

    protected BaseHandler getMainHandler() {
        if (null == mainHandler) {
            mainHandler = new BaseHandler(this, Looper.getMainLooper());
            mLoadingDialog.setCanceledOnTouchOutside(false);
        }
        return mainHandler;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_DIALOG: {
                getLoadingDialog().showDialog();
                break;
            }
            case DISMISS_DIALOG: {
                getLoadingDialog().dismissDialog();
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
        }
    }


    @Override
    protected void onDestroy() {
        if (null != mainHandler) {
            mainHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
}
