package com.lld360.cnc.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties(value = "password", allowSetters = true)
public class User implements Serializable {

    private Long id;

    //姓名
    @NotBlank(message = "姓名不能为空")
    @Length(max = 45, message = "姓名最大长度不能超过45个字符")
    private String name;

    /**
     * 手机号
     */
    @Length(max = 11, message = "手机号 最大长度不能超过11个字符")
    private String mobile;

    /**
     * 密码
     */
    @Length(max = 63, message = "密码 最大长度不能超过63个字符")
    private String password;

    /**
     * 昵称
     */
    @Length(max = 45, message = "昵称 最大长度不能超过45个字符")
    private String nickname;

    /**
     * 头像
     */
    @Length(max = 150, message = "头像 最大长度不能超过150个字符")
    private String avatar;

    /**
     * 地址
     */
    @Length(max = 100, message = "地址 最大长度不能超过100个字符")
    private String address;

    /**
     * 简介
     */
    @Length(max = 200, message = "简介 最大长度不能超过200个字符")
    private String description;

    /**
     * 创建时间
     */
    @NotNull(message = "创建时间 不能为空。")
    private Date createTime;

    /**
     * 最后登录时间
     */
    private Date lastLogin;

    /**
     * 状态
     */
    @NotNull(message = "状态 不能为空。")
    private Byte state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }
}