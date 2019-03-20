package com.dkaishu.tcraft.feature.sample;

import android.os.Environment;

import com.dkaishu.tcraft.R;
import com.dkaishu.tcraft.data.MyApiService;
import com.dkaishu.tcraft.data.base.BaseHttpObserver;
import com.dkaishu.tcraft.data.base.BaseResponse;
import com.dkaishu.tcraftlib.base.BaseHttpActivity;
import com.dkaishu.tcraftlib.network.RetrofitClient;
import com.dkaishu.tcraftlib.network.download.DownloadListener;
import com.dkaishu.tcraftlib.network.download.DownloadRetrofitClient;
import com.dkaishu.tcraftlib.network.download.FileDownLoadObserver;
import com.dkaishu.tcraftlib.network.upload.FileUploadListener;
import com.dkaishu.tcraftlib.network.upload.UploadFileRequestBody;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;

/**
 * 文件下载
 * Created by dks on 2018/9/27.
 */

public class FileDownloadUploadActivity extends BaseHttpActivity {
    private static final String TAG = "FileDownloadActivity";

    private final static String                     downloadUrl
            = "http://ftp-new-apk.pconline.com.cn/417d498f47ad31e3f36e994c47ce20df/pub/download/201808/pconline1534815265261.apk";
    private              FileDownLoadObserver<File> mDownLoadObserver;


    private final static String                         uploadUrl = "";
    private final static String                         filePath  = "";
    private              BaseHttpObserver<BaseResponse> mUploadObserver;

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_file_download;
    }

    @Override
    protected void initView() {
        findViewById(R.id.bt_download_start).setOnClickListener(v -> download());
        findViewById(R.id.bt_download_stop).setOnClickListener(v -> mDownLoadObserver.cancel());

        findViewById(R.id.bt_upload_start).setOnClickListener(v -> upload(new File(filePath)));
        findViewById(R.id.bt_upload_stop).setOnClickListener(v -> mUploadObserver.cancel());

    }

    @Override
    protected void initData() {
        mDownLoadObserver = new FileDownLoadObserver<File>() {
            @Override
            public void onDownLoadSuccess(File file) {
            }

            @Override
            public void onDownLoadFail(Throwable throwable) {
            }
        };

        mUploadObserver = new BaseHttpObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse baseResponse) {

            }
        };

    }


    /**
     * 下载文件
     * DownloadListener 的 onProgress 运行在网络请求的子线程
     * FileDownLoadObserver 的 onSaveProgress 运行在io子线程
     */
    private void download() {
        DownloadRetrofitClient.newInstance().createService(MyApiService.class, null, new DownloadListener() {
            @Override
            public void onProgress(int progress, long total) {//下载进度
                setLoadingText(progress + " %");
            }
        }).download(downloadUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .doOnSubscribe(disposable -> showLoadingDialog())
                .doFinally(this::dismissLoadingDialog)
                .map(observer -> mDownLoadObserver            //保存路径 及重命名
                        .saveFile(observer, Environment.getExternalStorageDirectory().getPath(), "66.apk"))
                .subscribe(mDownLoadObserver);
    }

    private void upload(File file) {
        Map<String, String> params = new HashMap<>();
        params.put("myKey", "myValue");

        RetrofitClient.getInstance().createService(MyApiService.class)
                .upload(uploadUrl, MultipartBody.Part.createFormData("file", file.getName(),
                        new UploadFileRequestBody<ResponseBody>(file, new FileUploadListener() {
                            @Override
                            public void onProgress(long bytesWritten, long contentLength) {
                                int progress = (int) (bytesWritten / contentLength);
                                setLoadingText(progress + " %");
                            }
                        })), params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .doOnSubscribe(disposable -> showLoadingDialog())
                .doFinally(this::dismissLoadingDialog)
                .subscribe(mUploadObserver);

    }

}
