package com.dkaishu.tcraft.data;

import com.dkaishu.tcraft.data.base.BaseResponse;
import com.dkaishu.tcraft.data.model.Categories;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by dks on 2018/9/6.
 */

public interface MyApiService {


    @GET("users/{user}/repos")
    Call<List<String>> listRepos(@Path("user") String user);


    @GET("http://dkaishu.com")
    Observable<String> getTopMovie();


    @GET("https://gank.io/api/xiandu/categories")
    Observable<Categories> getXD();



    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);

    @Multipart
    @POST
    Observable<BaseResponse> upload(@Url String url, @Part MultipartBody.Part file, @QueryMap Map<String, String> params);

}
