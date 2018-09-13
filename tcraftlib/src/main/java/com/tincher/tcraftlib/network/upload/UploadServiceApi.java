package com.tincher.tcraftlib.network.upload;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;


public interface UploadServiceApi {
    @Multipart
    @POST
    Observable<ResponseBody> upload(@Url String url, @Part MultipartBody.Part file, @QueryMap Map<String, String> params);

}
