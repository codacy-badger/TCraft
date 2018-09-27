package com.tincher.tcraft.data;

import com.tincher.tcraft.data.model.Categories;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by dks on 2018/9/6.
 */

public class MyApiService {


    public interface GitHubService {
        @GET("users/{user}/repos")
        Call<List<String>> listRepos(@Path("user") String user);

    }

    public interface TestService {
        @GET("http://dkaishu.com")
        Observable<String> getTopMovie();

    }

    public interface xianduService {
        @GET("https://gank.io/api/xiandu/categories")
        Observable<Categories> getXD();

    }
}
