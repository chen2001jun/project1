package com.lld360.cnc.dto;

import com.lld360.cnc.model.User;

public class UserDto extends User {

    private Integer totalScore;
    private Integer totalIn;
    private Integer totalOut;
    private Boolean isBind;
    private Integer loginType;
    private String  openid;

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getTotalIn() {
        return totalIn;
    }

    public void setTotalIn(Integer totalIn) {
        this.totalIn = totalIn;
    }

    public Integer getTotalOut() {
        return totalOut;
    }

    public void setTotalOut(Integer totalOut) {
        this.totalOut = totalOut;
    }

    public Integer getLoginType() {
        return loginType;
    }

    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }

    public Boolean getIsBind() {
        return isBind;
    }

    public void setIsBind(Boolean bind) {
        isBind = bind;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
