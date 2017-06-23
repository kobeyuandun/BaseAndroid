package com.baseandroid.config;

import android.os.Build;

import com.baseandroid.BuildConfig;
import com.jayfeng.lesscode.core.LogLess;
import com.jayfeng.lesscode.core.NetworkLess;
import com.mcxiaoke.packer.helper.PackerNg;

public class AppUtils {

    /**
     * 返回自定义Custom-Agent，格式如下：
     * 系统名;系统版本名;系统版本号;应用版本名;应用版本号;网络类型;手机型号;DeviceId;渠道
     * @return Android;4.4.4;19;V1.0-Alpha-201506121401;2;WIFI_FAST;Xiaomi;99000629045616;Official
     */
    public static String formatUserAgent() {
        StringBuilder result = new StringBuilder();
        result.append("Android");
        result.append(";");
        result.append(Build.VERSION.RELEASE);
        result.append(";");
        result.append(Build.VERSION.SDK_INT);
        result.append(";");
        result.append(BuildConfig.VERSION_NAME);
        result.append(";");
        result.append(BuildConfig.VERSION_CODE);
        result.append(";");
        result.append(NetworkLess.$type().name());
        result.append(";");
        result.append(Build.MODEL);
        result.append(";");
        // result.append(Global.getAndroidID());
        // result.append(";");
        result.append(AppUtils.getChannel());
        LogLess.$d("custom user agent:" + result.toString());
        return result.toString().replaceAll("\\s*", "");
    }

    public static String getChannel() {
        return PackerNg.getMarket(Global.getContext(), "official");
    }

}
