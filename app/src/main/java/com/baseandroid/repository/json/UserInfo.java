package com.baseandroid.repository.json;

import android.support.annotation.Keep;

@Keep
public class UserInfo {
    private String userNo;
    private String name;
    private String orgNo;
    private String orgName;
    private String districtNo;
    private String districtName;
    private String positionNo;
    private String positionName;
    private String alias;
    private String email;
    private String certtype;
    private String certno;
    private String mobile;

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrgNo() {
        return orgNo;
    }

    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getDistrictNo() {
        return districtNo;
    }

    public void setDistrictNo(String districtNo) {
        this.districtNo = districtNo;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(String positionNo) {
        this.positionNo = positionNo;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCerttype() {
        return certtype;
    }

    public void setCerttype(String certtype) {
        this.certtype = certtype;
    }

    public String getCertno() {
        return certno;
    }

    public void setCertno(String certno) {
        this.certno = certno;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "UserInfo{" + "userNo='" + userNo + '\'' + ", name='" + name + '\'' + ", orgNo='" + orgNo + '\'' + ", orgName='" + orgName + '\'' + ", districtNo='" + districtNo + '\'' + ", districtName='" + districtName + '\'' + ", positionNo='" + positionNo + '\'' + ", positionName='" + positionName + '\'' + ", alias='" + alias + '\'' + ", email='" + email + '\'' + ", certtype='" + certtype + '\'' + ", certno='" + certno + '\'' + ", mobile='" + mobile + '\'' + '}';
    }
}
