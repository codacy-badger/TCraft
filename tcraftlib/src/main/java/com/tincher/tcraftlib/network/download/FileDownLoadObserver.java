package com.tincher.tcraftlib.network.download;

import com.blankj.utilcode.util.CloseUtils;
import com.blankj.utilcode.util.FileUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.util.EndConsumerHelper;
import io.reactivex.observers.DefaultObserver;
import okhttp3.ResponseBody;

/**
 * Created by dks on 2018/9/13.
 */

public abstract class FileDownLoadObserver<T> implements Observer<T> {
    final AtomicReference<Disposable> s = new AtomicReference<>();
    @Override
    public void onSubscribe(@NonNull Disposable s) {
        if (EndConsumerHelper.setOnce(this.s, s, getClass())) {
        }
    }

    /**
     * Cancels the upstream's disposable.
     */
    public final void cancel() {
        DisposableHelper.dispose(s);
    }


    @Override
    public void onNext(T t) {
        onDownLoadSuccess(t);

    }

    @Override
    public void onError(Throwable e) {
        onDownLoadFail(e);

    }

    //可以重写，具体可由子类实现
    @Override
    public void onComplete() {

    }

    //下载成功的回调
    public abstract void onDownLoadSuccess(T t);

    //下载失败回调
    public abstract void onDownLoadFail(Throwable throwable);

    //进度监听
    public abstract void onSaveProgress(int progress, Long total);


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
                    onSaveProgress(progress, total);
                }
                len = is.read(data, 0, 1024);
            }
            return FileUtils.getFileByPath(downloadFilePath);
        } catch (IOException e) {
            e.printStackTrace();
//            Log.e("saveFile", e.getMessage());
            return null;
        } finally {
            CloseUtils.closeIO(is, os);
        }


    }

}
