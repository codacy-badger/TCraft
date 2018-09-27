package com.tincher.tcraftlib.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tincher.tcraftlib.network.download.DownloadInterceptor;
import com.tincher.tcraftlib.network.download.DownloadListener;
import com.tincher.tcraftlib.network.download.DownloadRetrofitClient;
import com.tincher.tcraftlib.network.download.DownloadServiceApi;
import com.tincher.tcraftlib.network.download.FileDownLoadObserver;
import com.tincher.tcraftlib.network.networkstatus.NetInfo;
import com.tincher.tcraftlib.network.upload.FileUploadListener;
import com.tincher.tcraftlib.network.upload.UploadFileRequestBody;
import com.tincher.tcraftlib.network.upload.UploadServiceApi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;


public class NetworkUtil {

    /**
     * 网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager)
                    context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo network = cm.getActiveNetworkInfo();
            if (network != null && network.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否有网络连接
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo network = mConnectivityManager
                    .getActiveNetworkInfo();
            if (network != null) {
                return network.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断WIFI网络是否可用
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifi = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifi != null) {
                return wifi.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断MOBILE网络是否可用
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobile = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mobile != null) {
                return mobile.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取当前网络连接的类型信息
     */
    public static int getNetworkTypeConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo network = mConnectivityManager
                    .getActiveNetworkInfo();
            if (network != null && network.isAvailable()) {
                return network.getType();
            }
        }
        return -1;
    }

    /**
     * 获取当前的网络状态
     */
    public static String getNetworkType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = cm.getActiveNetworkInfo();
        if (network == null) {
            return NetInfo.NONE_NET;
        }
        if (network.getType() == ConnectivityManager.TYPE_MOBILE) {
            return network.getExtraInfo().equalsIgnoreCase("cmnet") ? NetInfo.CM_NET : NetInfo.CM_WAP;
        } else if (network.getType() == ConnectivityManager.TYPE_WIFI) {
            return NetInfo.WIFI;
        }
        return NetInfo.NONE_NET;
    }

    /**
     * 下载文件
     * DownloadListener 的 onProgress 运行在网络请求的子线程
     * FileDownLoadObserver 的 onSaveProgress 运行在io子线程
     */
    public static void download(String url, final String destDir, final String fileName, DownloadListener listener, final FileDownLoadObserver<File> observer) {
        List<Interceptor> interceptors = new ArrayList<Interceptor>();
        interceptors.add(new DownloadInterceptor(listener));

        //Todo 内存泄露
        DownloadRetrofitClient.getInstance().createService(DownloadServiceApi.class, null, interceptors)
                .download(url)
                .subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(ResponseBody responseBody) throws Exception {
                        return observer.saveFile(responseBody, destDir, fileName);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    /**
     * 上传文件
     */
/*    public static void upload(String url, File file, final FileUploadListener<ResponseBody> listener, Map<String, String> params) {
        RetrofitClient.getInstance().createService(UploadServiceApi.class)
                .upload(url, MultipartBody.Part.createFormData("formDataName", file.getName(), new UploadFileRequestBody<ResponseBody>(file, listener)), params)
//                .compose(applyIoSchedulers())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new DefaultObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        listener.onUpLoadSuccess(responseBody);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onUpLoadFail(e);
                    }

                    @Override
                    public void onComplete() {
                        listener.onComplete();
                    }
                });
    }*/
}
