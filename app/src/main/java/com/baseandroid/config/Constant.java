package com.baseandroid.config;

import android.support.annotation.Keep;

@Keep
public class Constant {

    /**
     * api status code
     */
    public static final int WEB_SUCCESS = 200;

    /**
     * long time
     */
    public static final long TIME_ONE_SECOND = 1000;
    public static final long TIME_ONE_MINITUE = 60000;
    public static final long TIME_ONE_HOUR = 3600000;

    /*
    * preference_key
    */
    public static String PREFERENCE_KEY_USER_AC_TOKEN = "ac_token";
    public static String PREFERENCE_KEY_USER_TOKEN_INFO = "user_token_info";
    public static String PREFERENCE_KEY_USER_JPUSH_ALIAS = "jpush_alias";
}