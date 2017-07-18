package com.baseandroid.repository.config;

import com.baseandroid.config.Global;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.File;
import java.io.IOException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpClientManagerAli {

    private OkHttpClientManagerAli() {

    }

    private static class SingletonHolder {
        private static final OkHttpClient instance = createOkHttpClient();
    }

    public static OkHttpClient getOkHttpClient() {
        return SingletonHolder.instance;
    }

    private static OkHttpClient createOkHttpClient() {

        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(Global
                .getContext()));

        OkHttpClient okHttpClient = new OkHttpClient.Builder().hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        })
                .addInterceptor(headerInterceptor)
                .addInterceptor(logInterceptor)
                .addNetworkInterceptor(new StethoInterceptor())
                .cookieJar(cookieJar)
                .cache(getOkHttpCache())
                .build();
        return okHttpClient;
    }

    private static Interceptor headerInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();

            Request.Builder newRequestBuilder = originalRequest.newBuilder();
            newRequestBuilder.addHeader("Authorization", "APPCODE 378516b516714864989fe95dde4baf24");

            Response response = chain.proceed(newRequestBuilder.build());
            return response;
        }
    };

    private static Cache getOkHttpCache() {
        File cacheFile = new File(Global.getContext().getCacheDir(), "OkCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        return cache;
    }

}
