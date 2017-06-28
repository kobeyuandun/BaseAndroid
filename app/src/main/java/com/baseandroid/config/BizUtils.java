package com.baseandroid.config;

import com.tencent.bugly.crashreport.CrashReport;

// 业务工具类, 第三方分享, 支付, Webview
public class BizUtils {

    public static void buglyInit() {
        CrashReport.initCrashReport(Global.getContext(), "e755bb926e", true);
    }

}
