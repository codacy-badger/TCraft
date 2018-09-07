package com.tincher.tcraftlib.network;

import okhttp3.ResponseBody;

/**
 * 回调在子线程中，因此不能直接更新UI
 * Created by dks on 2018/9/6.
 */

public interface TincherInterceptorCallback {
    ResponseBody OnResponse(ResponseBody responseBody);

}
