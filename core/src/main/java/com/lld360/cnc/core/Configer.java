package com.lld360.cnc.core;

import com.taobao.api.TaobaoClient;

/**
 * Author: dhc
 * Date: 2016-06-29 11:32
 */
public class Configer {
    private String env;

    private String appUrl;

    private String wxGzhToken;
    private String wxGzhAppID;
    private String wxGzhAppSecret;
    private String wxGzhEncodingAESKey;
    private String fileBasePath;

    private TaobaoClient taobaoSmsClient;
    private Integer expiredTime;

    private String wxAccountAppid;
    private String wxAccountScrect;

    private String qqAccountAppid;
    private String qqAccountScrect;


    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getWxGzhToken() {
        return wxGzhToken;
    }

    public void setWxGzhToken(String wxGzhToken) {
        this.wxGzhToken = wxGzhToken;
    }

    public String getWxGzhAppID() {
        return wxGzhAppID;
    }

    public void setWxGzhAppID(String wxGzhAppID) {
        this.wxGzhAppID = wxGzhAppID;
    }

    public String getWxGzhAppSecret() {
        return wxGzhAppSecret;
    }

    public void setWxGzhAppSecret(String wxGzhAppSecret) {
        this.wxGzhAppSecret = wxGzhAppSecret;
    }

    public String getWxGzhEncodingAESKey() {
        return wxGzhEncodingAESKey;
    }

    public void setWxGzhEncodingAESKey(String wxGzhEncodingAESKey) {
        this.wxGzhEncodingAESKey = wxGzhEncodingAESKey;
    }

    public String getFileBasePath() {
        return fileBasePath;
    }

    public void setFileBasePath(String fileBasePath) {
        this.fileBasePath = fileBasePath;
    }

    public TaobaoClient getTaobaoSmsClient() {
        return taobaoSmsClient;
    }

    public void setTaobaoSmsClient(TaobaoClient taobaoSmsClient) {
        this.taobaoSmsClient = taobaoSmsClient;
    }

    public Integer getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Integer expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getWxAccountAppid() {
        return wxAccountAppid;
    }

    public void setWxAccountAppid(String wxAccountAppid) {
        this.wxAccountAppid = wxAccountAppid;
    }

    public String getWxAccountScrect() {
        return wxAccountScrect;
    }

    public void setWxAccountScrect(String wxAccountScrect) {
        this.wxAccountScrect = wxAccountScrect;
    }

    public String getQqAccountAppid() {
        return qqAccountAppid;
    }

    public void setQqAccountAppid(String qqAccountAppid) {
        this.qqAccountAppid = qqAccountAppid;
    }

    public String getQqAccountScrect() {
        return qqAccountScrect;
    }

    public void setQqAccountScrect(String qqAccountScrect) {
        this.qqAccountScrect = qqAccountScrect;
    }
}
