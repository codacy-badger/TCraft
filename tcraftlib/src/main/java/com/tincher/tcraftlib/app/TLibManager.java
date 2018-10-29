package com.tincher.tcraftlib.app;

/**
 * Todo 初始化相关
 * Lib使用时需要初始化及管理的部分
 * Created by dks on 2018/8/3.
 */

public class TLibManager {
    private static String baseUrl = "http://dkaishu.com";
    public static void init(TBuilder builder){
        baseUrl = builder.baseUrl;

    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static final class TBuilder {
        String baseUrl;
        public TBuilder baseUrl(String url) {
            baseUrl = url;
            return this;
        }


    }

}
