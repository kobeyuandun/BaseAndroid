package com.baseandroid.config;

import android.content.Context;
import android.text.TextUtils;

import com.baseandroid.repository.json.UserTokenInfo;
import com.jayfeng.lesscode.core.SharedPreferenceLess;
import com.orhanobut.hawk.Hawk;

public class Global {
    private static Context sContext;
    private static String sAccessToken;
    private static UserTokenInfo sUserTokenInfo;

    public static Context getContext() {
        return sContext;
    }

    public static void setContext(Context context) {
        sContext = context;
    }

    public static String getUserAcToken() {
        if (TextUtils.isEmpty(sAccessToken)) {
            sAccessToken = SharedPreferenceLess.$get(Constant.PREFERENCE_KEY_USER_AC_TOKEN,
                    "");
        }
        return sAccessToken;
    }

    public static void setUserAcToken(String actoken) {
        SharedPreferenceLess.$put(Constant.PREFERENCE_KEY_USER_AC_TOKEN, actoken);
        sAccessToken = actoken;
    }

    public static UserTokenInfo getUserInfo() {
        if (sUserTokenInfo == null) {
            sUserTokenInfo = Hawk.get(Constant.PREFERENCE_KEY_USER_TOKEN_INFO);
        }
        return sUserTokenInfo;
    }

    public static void setUserInfo(UserTokenInfo usertokeninfo) {
        Hawk.put(Constant.PREFERENCE_KEY_USER_TOKEN_INFO, usertokeninfo);
        Global.sUserTokenInfo = usertokeninfo;
    }

}
