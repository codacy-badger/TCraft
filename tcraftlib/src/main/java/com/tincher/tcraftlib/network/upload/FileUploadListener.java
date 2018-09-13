package com.tincher.tcraftlib.network.upload;

/**
 * Created by dks on 2018/9/13.
 */

public abstract class FileUploadListener<T> {
   public void onComplete() {

    }

    void onProgress(long bytesWritten, long contentLength) {
        int progress = (int) (bytesWritten * 100 / contentLength);
        onProgress(progress);
    }

    //上传成功的回调
    public abstract void onUpLoadSuccess(T t);

    //上传你失败回调
    public abstract void onUpLoadFail(Throwable throwable);

    //上传进度回调
    public abstract void onProgress(int progress);

}
