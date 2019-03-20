package com.tincher.tcraftlib.network.upload;

/**
 * Created by dks on 2018/9/13.
 */

public interface FileUploadListener {
    void onProgress(long bytesWritten, long contentLength);
}
