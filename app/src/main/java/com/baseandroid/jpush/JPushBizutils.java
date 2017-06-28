package com.baseandroid.jpush;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.baseandroid.BuildConfig;
import com.baseandroid.config.Global;
import com.jayfeng.lesscode.core.NetworkLess;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class JPushBizutils {
    private static final int MSG_SET_ALIAS_AND_TAGS = 1001;

    private static HandlerThread mHandlerThread;
    private static Handler mHandler;

    public static void initJPush() {
        JPushInterface.init(Global.getContext());
        if (BuildConfig.DEBUG) {
            JPushInterface.setDebugMode(true);
        }

        if (!Global.getsJpushAlias().equals(Global.getUserInfo().getUser().getMobile())) {
            JPushInterface.stopPush(Global.getContext());
            mHandlerThread = new HandlerThread(JPushBizutils.class.getName());
            mHandlerThread.start();

            mHandler = new Handler(mHandlerThread.getLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case MSG_SET_ALIAS_AND_TAGS:
                            resumeJPush((String) msg.obj);
                            JPushInterface.setAliasAndTags(Global.getContext(), (String) msg.obj, null, mAliasCallback);
                            break;
                        default:
                            break;
                    }
                }
            };

            mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS_AND_TAGS, Global
                    .getUserInfo()
                    .getUser()
                    .getMobile()), 2000);

        } else {
            if (JPushInterface.isPushStopped(Global.getContext())) {
                JPushInterface.resumePush(Global.getContext());
            }
        }
    }

    public static void resumeJPush(String newAlias) {
        Global.setsJpushAlias(newAlias);
        JPushInterface.resumePush(Global.getContext());
    }

    public static void stopJPush(String newAlias) {
        Global.setsJpushAlias(newAlias);
        JPushInterface.stopPush(Global.getContext());
    }

    private static TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    mHandler.removeCallbacks(null);
                    mHandlerThread.getLooper().quit();
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    if (NetworkLess.$online()) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS_AND_TAGS, alias), 1000 * 60);
                    } else {
                        logs = "No network";
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
            }

            Log.d("JPush", "logs = " + logs);
        }

    };
}
