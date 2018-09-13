package com.tincher.tcraftlib.data;

import com.tincher.tcraftlib.network.RetrofitClient;
import com.tincher.tcraftlib.network.download.DownloadInterceptor;
import com.tincher.tcraftlib.network.download.DownloadListener;
import com.tincher.tcraftlib.network.download.DownloadRetrofitClient;
import com.tincher.tcraftlib.network.download.DownloadServiceApi;
import com.tincher.tcraftlib.network.download.FileDownLoadObserver;
import com.tincher.tcraftlib.network.upload.FileUploadListener;
import com.tincher.tcraftlib.network.upload.UploadFileRequestBody;
import com.tincher.tcraftlib.network.upload.UploadServiceApi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;

/**
 * Created by dks on 2018/9/13.
 */

public class DataManager {

    @Deprecated
    public static void download(String url, final String destDir, final String fileName, final FileDownLoadObserver<File> observer) {
        RetrofitClient.getInstance().createService(DownloadServiceApi.class)
                .download(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(ResponseBody responseBody) throws Exception {
                        return observer.saveFile(responseBody, destDir, fileName);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(observer);

    }

    public static void download(String url, final String destDir, final String fileName, DownloadListener listener, final FileDownLoadObserver<File> observer) {
        List<Interceptor> interceptors = new ArrayList<Interceptor>();
        interceptors.add(new DownloadInterceptor(listener));

        DownloadRetrofitClient.getInstance().createService(DownloadServiceApi.class, null, interceptors )
                .download(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(ResponseBody responseBody) throws Exception {
                        return observer.saveFile(responseBody, destDir, fileName);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe();
                .safeSubscribe(observer);

    }

    public static void upload(String url, File file, final FileUploadListener<ResponseBody> listener, Map<String, String> params) {
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


    }
//            RetrofitUploadFileClient.createUploadService(FileAPI::class.java, mAccessToken)
//            .uploadFile(url, MultipartBody.Part.createFormData("formDataName", file.name, UploadFileRequestBody(file, observer)), params)
//            .compose(SchedulersCompact.applyIoSchedulers())
//            .subscribe(
//    { responseBody -> observer.onUpLoadSuccess(responseBody) },
//    { throwable -> observer.onUpLoadFail(throwable) },
//    { observer.onComplete() })


    //    private static class SchedulersCompact<T> {
    private static <T> ObservableTransformer<T, T> applyIoSchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

            }
        };

    }
//    }
}
