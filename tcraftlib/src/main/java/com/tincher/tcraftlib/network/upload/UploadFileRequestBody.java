package com.tincher.tcraftlib.network.upload;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 *
 * Todo 文件上传编写和测试
 * Created by dks on 2018/9/13.
 */

public class UploadFileRequestBody<T> extends RequestBody {
    private File                  file;
    private FileUploadListener<T> fileUploadListener;

    private RequestBody mRequestBody;


    public UploadFileRequestBody(File file, FileUploadListener<T> fileUploadListener) {
        this.file = file;
        this.fileUploadListener = fileUploadListener;
        mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file); //multipart/form-data  || application/octet-stream
    }

    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        CountingSink countingSink = new CountingSink(sink);
        BufferedSink bufferedSink = Okio.buffer(countingSink);
        //写入
        mRequestBody.writeTo(bufferedSink);
        //刷新
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
    }

     private class CountingSink extends ForwardingSink {
        private long bytesWritten = 0;

        public CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            bytesWritten += byteCount;
            fileUploadListener.onProgress(bytesWritten, contentLength());
        }

    }
}
