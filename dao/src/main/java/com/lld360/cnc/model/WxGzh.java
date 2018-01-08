package com.lld360.cnc.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * WxGzh
 */
public class WxGzh implements Serializable {
    /**
     * id
     */
    @NotBlank(message = "id 不能为空。")
    private Integer id;
    /**
     * 微信公众号名称
     */
    @NotBlank(message = "微信公众号名称 不能为空。")
    @Length(max = 45, message = "微信公众号名称 最大长度不能超过45个字符")
    private String name;
    /**
     * 微信公众号AppId
     */
    @NotBlank(message = "微信公众号AppId 不能为空。")
    @Length(max = 2, message = "微信公众号AppId 最大长度不能超过2个字符")
    private String appid;
    /**
     * 微信公众号AppSecret
     */
    @NotBlank(message = "微信公众号AppSecret 不能为空。")
    @Length(max = 32, message = "微信公众号AppSecret 最大长度不能超过32个字符")
    private String appsecret;
    /**
     * 微信公众号EncodingAESKey
     */
    @NotBlank(message = "微信公众号EncodingAESKey 不能为空。")
    @Length(max = 63, message = "微信公众号EncodingAESKey 最大长度不能超过63个字符")
    private String encodingAesKey;
    /**
     * 主账号
     */
    @Length(max = 45, message = "主账号 最大长度不能超过45个字符")
    private String adminAccount;
    /**
     * 主账号管理员
     */
    @Length(max = 45, message = "主账号管理员 最大长度不能超过45个字符")
    private String adminName;
    /**
     * createTime
     */
    @NotBlank(message = "createTime 不能为空。")
    private Date createTime;


    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return this.name;
    }

    public void setAppid(String value) {
        this.appid = value;
    }

    public String getAppid() {
        return this.appid;
    }

    public void setAppsecret(String value) {
        this.appsecret = value;
    }

    public String getAppsecret() {
        return this.appsecret;
    }

    public void setEncodingAesKey(String value) {
        this.encodingAesKey = value;
    }

    public String getEncodingAesKey() {
        return this.encodingAesKey;
    }

    public void setAdminAccount(String value) {
        this.adminAccount = value;
    }

    public String getAdminAccount() {
        return this.adminAccount;
    }

    public void setAdminName(String value) {
        this.adminName = value;
    }

    public String getAdminName() {
        return this.adminName;
    }

    public void setCreateTime(Date value) {
        this.createTime = value;
    }

    public Date getCreateTime() {
        return this.createTime;
    }
}