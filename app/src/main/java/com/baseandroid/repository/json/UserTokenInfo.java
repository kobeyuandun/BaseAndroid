package com.baseandroid.repository.json;

import android.support.annotation.Keep;

@Keep
public class UserTokenInfo {

    private UserInfo user;
    private String ac_token;
    private String rf_token;
    private String token_expire_at;

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public String getAc_token() {
        return ac_token;
    }

    public void setAc_token(String ac_token) {
        this.ac_token = ac_token;
    }

    public String getRf_token() {
        return rf_token;
    }

    public void setRf_token(String rf_token) {
        this.rf_token = rf_token;
    }

    public String getToken_expire_at() {
        return token_expire_at;
    }

    public void setToken_expire_at(String token_expire_at) {
        this.token_expire_at = token_expire_at;
    }

    @Override
    public String toString() {
        return "UserTokenInfo{" + "user=" + user.toString() + ", ac_token='" + ac_token + '\'' +
                ", rf_token='" + rf_token + '\'' + ", token_expire_at='" + token_expire_at + '\'' + '}';
    }
}
