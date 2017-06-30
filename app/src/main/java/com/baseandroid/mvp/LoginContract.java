package com.baseandroid.mvp;

import java.util.List;

public interface LoginContract {

    interface Presenter {
        void login();

        void loginWechat(String code);

        void loginQQ(String token, String openid);

        void loginWeibo(String token, String openid);
    }

    interface View {
        void showDate(List list);

        void showError(String errorMessage);
    }
}
