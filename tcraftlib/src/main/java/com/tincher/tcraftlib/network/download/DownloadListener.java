package com.tincher.tcraftlib.network.download;

/**
 * Created by dks on 2018/9/13.
 */

public interface DownloadListener {

    void onProgress(float progress,long total);

}
