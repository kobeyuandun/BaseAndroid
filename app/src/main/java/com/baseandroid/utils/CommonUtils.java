package com.baseandroid.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.EditText;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    public static void setFullScreen(Activity activity, boolean isFullScreen) {
        if (isFullScreen) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.getWindow()
                    .clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        } else {
            activity.getWindow()
                    .addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public static AlertDialog.Builder getConfirmDialog(Context context, String title, String message, String positiveText, String negativeText, boolean cancelable, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        return new AlertDialog.Builder(context).setCancelable(cancelable)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, positiveListener)
                .setNegativeButton(negativeText, negativeListener);
    }

    public static void goSetPremission(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    public static String getDeviceId(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static boolean isGreater(String serverVersion, String curVersion, boolean needContains) {
        System.out.println("服务器版本号：" + serverVersion);
        System.out.println("app版本号：" + curVersion);

        if (serverVersion == null || serverVersion.trim().equals("")) {
            return false;
        }
        if (curVersion == null || curVersion.trim().equals("")) {
            return true;
        }
        if (serverVersion.contains("v") || serverVersion.contains("V")) {
            serverVersion = serverVersion.substring(1, serverVersion.length());
        }
        if (curVersion.contains("v") || curVersion.contains("V")) {
            curVersion = curVersion.substring(1, curVersion.length());
        }

        if (needContains) {
            if (TextUtils.equals(serverVersion, curVersion)) {
                return true;
            } else {
                StringTokenizer firstToken = new StringTokenizer(serverVersion, ".");
                StringTokenizer secondToken = new StringTokenizer(curVersion, ".");
                while (firstToken.hasMoreElements()) {
                    String firstStr = firstToken.nextToken();
                    String secondStr = null;
                    if (secondToken.hasMoreElements()) {
                        secondStr = secondToken.nextToken();
                    }
                    if (secondStr == null) {
                        return true;
                    }
                    int firstValue = Integer.parseInt(firstStr);
                    int secondValue = Integer.parseInt(secondStr);
                    if (firstValue > secondValue) {
                        return true;
                    } else if (firstValue < secondValue) {
                        return false;
                    }
                }
            }
        } else {
            StringTokenizer firstToken = new StringTokenizer(serverVersion, ".");
            StringTokenizer secondToken = new StringTokenizer(curVersion, ".");
            while (firstToken.hasMoreElements()) {
                String firstStr = firstToken.nextToken();
                String secondStr = null;
                if (secondToken.hasMoreElements()) {
                    secondStr = secondToken.nextToken();
                }
                if (secondStr == null) {
                    return true;
                }
                int firstValue = Integer.parseInt(firstStr);
                int secondValue = Integer.parseInt(secondStr);
                if (firstValue > secondValue) {
                    return true;
                } else if (firstValue < secondValue) {
                    return false;
                }
            }
        }

        return false;
    }

    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    public static void addEditSpace(CharSequence s, int start, int before, EditText _text) {
        if (s == null || s.length() == 0) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                continue;
            } else {
                sb.append(s.charAt(i));
                if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                    sb.insert(sb.length() - 1, ' ');
                }
            }
        }
        if (!sb.toString().equals(s.toString())) {
            int index = start + 1;
            if (sb.charAt(start) == ' ') {
                if (before == 0) {
                    index++;
                } else {
                    index--;
                }
            } else {
                if (before == 1) {
                    index--;
                }
            }
            _text.setText(sb.toString());
            _text.setSelection(index);

        }
    }

}
