package com.tincher.tcraftlib.app;

import android.os.Environment;

/**
 * Todo 初始化相关
 * Lib使用时需要初始化及管理的部分
 * Created by dks on 2018/8/3.
 */

public class TLibManager {
    private static String baseUrl     = "http://dkaishu.com";
    private static String logFilePath = Environment.getExternalStorageDirectory().getPath() + "TCraft/log";

    public static void init(TBuilder builder) {
        baseUrl = builder.baseUrl;
        logFilePath = builder.logFilePath;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static String getLogFilePath() {
        return logFilePath;
    }

    public static final class TBuilder {
        String baseUrl;
        String logFilePath;

        public TBuilder baseUrl(String url) {
            baseUrl = url;
            return this;
        }

        /**
         *
         * @param path 完整路径。如 Environment.getExternalStorageDirectory().getPath() + "TCraft/log";
         */
        public TBuilder logFilePath(String path) {
            logFilePath = path;
            return this;
        }


    }

}
