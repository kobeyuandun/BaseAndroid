package com.baseandroid.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.baseandroid.utils.StatusBarHelper;
import com.jaeger.library.StatusBarUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends RxAppCompatActivity {

    private Unbinder mUnbinder;
    private BaseDoubleClickExitHelper mDoubleClickExitHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mUnbinder = ButterKnife.bind(this);
        setup(savedInstanceState);
        BaseApplication.getAppManager().addActivity(this);
        mDoubleClickExitHelper = new BaseDoubleClickExitHelper(this);
    }

    private void setup(Bundle savedInstanceState) {
        setupView();
        setupData(savedInstanceState);
    }

    protected abstract int getLayoutId();

    protected abstract void setupView();

    protected abstract void setupData(Bundle savedInstanceState);

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (false/*this instanceof HomeMainActivity*/) {
                    return mDoubleClickExitHelper.onKeyDown(keyCode, event);
                }
                break;

            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int resId) {
        Toast.makeText(this, this.getResources().getText(resId), Toast.LENGTH_LONG)
                .show();
    }

    // 设置状态栏透明度
    protected void translateStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        int supportWhiteStatusBar = StatusBarHelper.StatusBarLightMode(this);
        if (supportWhiteStatusBar > 0) {
            StatusBarUtil.setColor(this, getResources().getColor(android.R.color.white), 0);
        } else {
            StatusBarUtil.setColor(this, getResources().getColor(android.R.color.black), 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        BaseApplication.getAppManager().removeActivity(this);
    }
}
