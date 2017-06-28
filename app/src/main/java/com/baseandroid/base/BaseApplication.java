package com.baseandroid.base;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.baseandroid.BuildConfig;
import com.baseandroid.config.BizUtils;
import com.baseandroid.config.Global;
import com.facebook.stetho.Stetho;
import com.jayfeng.lesscode.core.$;
import com.orhanobut.hawk.Hawk;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import cn.jpush.android.api.JPushInterface;

public class BaseApplication extends Application{

    private static RefWatcher mRefWatcher;
    private static BaseAppManager mAppManager;

    @Override
    public void onCreate() {
        super.onCreate();

        Global.setContext(this);

        $.getInstance().context(this);

        Hawk.init(this).build();
        JPushInterface.init(this);
        BizUtils.buglyInit();

        if (BuildConfig.DEBUG) {
            mRefWatcher = LeakCanary.install(this);

            Stetho.initializeWithDefaults(this);

            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .build());

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .build());
        }
    }

    public static RefWatcher getRefWatcher(Context context) {
        // LeakCanary: Detect all memory leaks!
        // LeakCanary.install() returns a pre configured RefWatcher. It also
        // installs an ActivityRefWatcher that automatically detects if an activity is
        // leaking after Activity.onDestroy() has been called.
        return mRefWatcher;
    }

    public static BaseAppManager getAppManager() {
        if (mAppManager == null) {
            synchronized (BaseApplication.class) {
                mAppManager = BaseAppManager.getAppManager();
            }
        }
        return mAppManager;
    }

    public static void exitApp() {
        mAppManager.AppExit();
    }

}
