package com.baseandroid.config;

import android.content.Context;

public class Global {
    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    public static void setContext(Context context) {
        sContext = context;
    }
}
