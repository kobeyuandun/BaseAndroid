package com.android.photocameralib.media.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setup(savedInstanceState);
    }

    private void setup(Bundle savedInstanceState) {
        setupView();
        setupData(savedInstanceState);
    }

    protected abstract int getLayoutId();

    protected abstract void setupView();

    protected abstract void setupData(Bundle savedInstanceState);

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
