package com.dkaishu.tcraftlib.base;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by dks on 2018/8/3.
 */

public class BaseHandler extends Handler {

    private CallBack callBack;

    public interface CallBack{
        void handleMessage(Message msg);
    }

    public BaseHandler(CallBack callBack,Looper looper){
        super(looper);
        this.callBack = callBack;
    }

    @Override
    public void handleMessage(Message msg) {
        if(null != callBack){
            callBack.handleMessage(msg);
        }
    }

}
