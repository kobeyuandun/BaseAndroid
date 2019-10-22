package com.baseandroid.mvp;

import android.os.Bundle;

import com.baseandroid.base.BaseActivity;

import java.util.List;

/**
 * 登录页
 */
public class LoginActivity extends BaseActivity implements LoginContract.View {

    private LoginContract.Presenter mPresenter;

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void setupView() {

    }

    @Override
    protected void setupData(Bundle savedInstanceState) {

    }

    @Override
    public void showDate(List list) {

    }

    @Override
    public void showError(String errorMessage) {

    }
}
