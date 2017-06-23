package com.baseandroid.base;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.widget.Toast;

import com.baseandroid.R;


public class BaseDoubleClickExitHelper {

    private Context mContext;
    private boolean misOnKeyBacking;
    private Handler mHandler;
    private Toast mBackToast;

    public BaseDoubleClickExitHelper(Context context) {
        mContext = context.getApplicationContext();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return false;
        }

        if (misOnKeyBacking) {
            mHandler.removeCallbacks(onBackTimeRunnable);
            if (mBackToast != null) {
                mBackToast.cancel();
            }
            BaseApplication.exitApp();
            return true;
        } else {
            misOnKeyBacking = true;
            if (mBackToast == null) {
                mBackToast = Toast.makeText(mContext, R.string.common_exit_tips, Toast
                        .LENGTH_SHORT);
            }
            mBackToast.show();
            mHandler.postDelayed(onBackTimeRunnable, 2000);
            return true;
        }
    }

    private Runnable onBackTimeRunnable = new Runnable() {

        @Override
        public void run() {
            misOnKeyBacking = false;
            if (mBackToast != null) {
                mBackToast.cancel();
            }
        }
    };
}
