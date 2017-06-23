package com.baseandroid.repository.config;

import com.baseandroid.BuildConfig;
import com.baseandroid.config.AppUtils;
import com.baseandroid.config.Global;
import com.baseandroid.repository.config.cookie.PersistentCookieStore;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jayfeng.lesscode.debug.DebugHttpLoggingInterceptor;

import java.io.File;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpClientManager {

    private OkHttpClientManager() {

    }

    private static class SingletonHolder {
        private static final OkHttpClient instance = createOkHttpClient();
    }

    public static OkHttpClient getOkHttpClient() {
        return SingletonHolder.instance;
    }

    private static OkHttpClient createOkHttpClient() {

        DebugHttpLoggingInterceptor debugHttpLoggingInterceptor = new DebugHttpLoggingInterceptor();
        debugHttpLoggingInterceptor.setDebugHttpLoggingCallback(new DebugHttpLoggingInterceptor.DebugHttpLoggingCallback() {
            @Override
            public void onLog(Response response, String log) {

            }
        });
        debugHttpLoggingInterceptor.setLevel(DebugHttpLoggingInterceptor.Level.BODY);

        CookieHandler cookieHandler = new CookieManager(new PersistentCookieStore(Global.getContext()),
                CookiePolicy.ACCEPT_ALL);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .addInterceptor(headerInterceptor)
                .addInterceptor(debugHttpLoggingInterceptor)
                .addNetworkInterceptor(new StethoInterceptor())
                .cookieJar(new JavaNetCookieJar(cookieHandler))
                .cache(getOkHttpCache())
                .build();
        return okHttpClient;
    }

    private static Interceptor headerInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();

            Request.Builder newRequestBuilder = originalRequest.newBuilder();

            newRequestBuilder.header("platform", "Android");
            newRequestBuilder.header("ua", AppUtils.formatUserAgent());
            newRequestBuilder.header("version", BuildConfig.VERSION_CODE + "");
            newRequestBuilder.header("channel", AppUtils.getChannel());

            return chain.proceed(newRequestBuilder.build());
        }
    };

    private static Cache getOkHttpCache() {
        File cacheFile = new File(Global.getContext().getCacheDir(), "OkCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        return cache;
    }

}
