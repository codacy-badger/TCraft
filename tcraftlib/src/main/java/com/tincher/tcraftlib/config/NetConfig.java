package com.tincher.tcraftlib.config;

/**
 * Created by dks on 2018/9/5.
 */

public final class NetConfig {

//        const val CONNECT_TIMEOUT: Long = 10
//            const val READ_TIMEOUT: Long = 10
//            const val WRITE_TIMEOUT: Long = 10
//
//            /**
//             * 请求失败时是否自动重试
//             */
//            const val RETRY_ON_CONNECTION_FAILURE: Boolean = false
//
//            /**
//             * CACHE 大小，单位：M
//             */
//            const val CACHE_SIZE: Long = 10
    /**
     * 重连、读、写时间，单位：秒
     */
    public final static long CONNECT_TIMEOUT = 10;
    public final static long READ_TIMEOUT = 10;
    public final static long WRITE_TIMEOUT = 10;

    /**
     * 请求失败时是否自动重试
     */
    public final static boolean RETRY_ON_CONNECTION_FAILURE = true;

    /**
     * CACHE 大小，单位：M
     */
    public final static long CACHE_SIZE = 10;

}
