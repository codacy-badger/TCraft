package com.tincher.tcraftlib.network;

/**
 * 每次http请求时携带的校验token
 * 字段、数据格式待定
 * {
 * "access_token" : "29ed478ab86c07f1c069b1af76088f7431396b7c4a2523d06911345da82224a0",
 * "token_type" : "bearer",
 * "scope" : "public write"
 * }
 * <p>
 * Created by dks on 2018/a4/26.
 */

class AccessToken {
    /**
     * 授权标志
     */
    private String accessToken = "";
    /**
     * 授权类型
     */
    private String tokenType;
    /**
     * 范围
     */
    private String scope;
    /**
     * User id
     */
    private String id;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "accessToken='" + accessToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", scope='" + scope + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}