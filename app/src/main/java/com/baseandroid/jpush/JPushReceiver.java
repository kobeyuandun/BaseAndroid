package com.baseandroid.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

public class JPushReceiver extends BroadcastReceiver {

    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Log.d(TAG, "regId = " + regId);

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                String title = bundle.getString(JPushInterface.EXTRA_TITLE);
                String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
                String msgid = bundle.getString(JPushInterface.EXTRA_MSG_ID);
                Log.d(TAG, "ACTION_MESSAGE_RECEIVED message = " + message);
                Log.d(TAG, "ACTION_MESSAGE_RECEIVED extras = " + extras);
                Log.d(TAG, "ACTION_MESSAGE_RECEIVED msgid = " + msgid);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                String content = bundle.getString(JPushInterface.EXTRA_ALERT);
                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
                int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                String msgid = bundle.getString(JPushInterface.EXTRA_MSG_ID);
                Log.d(TAG, "ACTION_NOTIFICATION_RECEIVED title = " + title);
                Log.d(TAG, "ACTION_NOTIFICATION_RECEIVED message = " + content);
                Log.d(TAG, "ACTION_NOTIFICATION_RECEIVED extras = " + extras);
                Log.d(TAG, "ACTION_NOTIFICATION_RECEIVED notificationId = " + notificationId);
                Log.d(TAG, "ACTION_NOTIFICATION_RECEIVED msgid = " + msgid);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                String content = bundle.getString(JPushInterface.EXTRA_ALERT);
                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
                int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                String msgid = bundle.getString(JPushInterface.EXTRA_MSG_ID);
                Log.d(TAG, "ACTION_NOTIFICATION_OPENED title = " + title);
                Log.d(TAG, "ACTION_NOTIFICATION_OPENED message = " + content);
                Log.d(TAG, "ACTION_NOTIFICATION_OPENED notificationId = " + notificationId);
                Log.d(TAG, "ACTION_NOTIFICATION_OPENED msgid = " + msgid);

                //打开自定义的Activity
               /* Intent i = new Intent(context, TestActivity.class);
                i.putExtras(bundle);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);*/

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Log.w(TAG, "connected state change to " + connected);
            } else {
                Log.d(TAG, "Unhandled intent - " + intent.getAction());
            }

        } catch (Exception e) {

        }
    }

    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" + myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

}
