package com.baseandroid.mvp;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mView;

    public LoginPresenter(LoginContract.View view) {
        this.mView = view;
    }


    @Override
    public void login() {

    }

    @Override
    public void loginWechat(String code) {

    }

    @Override
    public void loginQQ(String token, String openid) {

    }

    @Override
    public void loginWeibo(String token, String openid) {

    }
}