package com.baseandroid.repository.config;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.baseandroid.BuildConfig;
import com.baseandroid.config.Global;
import com.baseandroid.repository.config.cookie.PersistentCookieStore;
import com.baseandroid.utils.EncryptUtil;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.qianbaocard.coresdk.SignUtil;

import java.io.File;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cache;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

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

        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        CookieHandler cookieHandler = new CookieManager(new PersistentCookieStore(Global.getContext()), CookiePolicy.ACCEPT_ALL);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        })
                .addInterceptor(headerInterceptor)
                .addInterceptor(logInterceptor)
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
            newRequestBuilder.addHeader("Accept-Resthub-Spec", "application/vnd.resthub" + ".v2+json");
            newRequestBuilder.addHeader("Uni-Source", "qbsh_crm/Android (OS/22 " + "APP/1.2.1)");

            String access_token = "";
            if (Global.getUserInfo() != null && Global.getUserInfo()
                    .getAc_token() != null) {
                access_token = Global.getUserInfo().getAc_token();
            }

            HttpUrl.Builder builderUrl = originalRequest.url()
                    .newBuilder()
                    .addQueryParameter("_ac_token", access_token)
                    .addQueryParameter("_platform", "app")
                    .addQueryParameter("_os", "android")
                    .addQueryParameter("_sysVersion", "" + Build.VERSION.SDK_INT)
                    .addQueryParameter("_appVersion", "" + BuildConfig.VERSION_NAME)
                    .addQueryParameter("_model", "" + "" + Build.MODEL)
                    .addQueryParameter("_caller", "qbsh_crm")
                    .addQueryParameter("_cUDID", "")
                    .addQueryParameter("_appChannel", "")
                    .addQueryParameter("_openUDID", "f7994e16d2d2fba5")
                    .addQueryParameter("__intern__show-error-mesg", "1");

            String requestUrl = builderUrl.build().toString();
            StringBuilder queryString = new StringBuilder(requestUrl.substring(requestUrl.indexOf("?") + 1));

            StringBuilder postString = new StringBuilder();
            if (RequestMethod.supportBody(originalRequest.method())) {
                FormBody formBody = (FormBody) originalRequest.body();
                for (int i = 0; i < formBody.size(); i++) {
                    Log.e("+++++", "===formBody.name====" + formBody.name(i));
                    Log.e("+++++", "===formBody.value====" + formBody.value(i));

                    postString.append(postString.length() > 0 ? "&" : "")
                            .append(formBody.name(i))
                            .append("=")
                            .append(formBody.value(i) == null ? "" : formBody.value(i));
                }
            }
            Log.e("+++++", "===queryString====" + queryString);
            Log.e("+++++", "===postString====" + postString);

            String signvalue = SignUtil.generateSign(Global.getContext(), RequestMethod.supportBody(originalRequest
                    .method()) ? "POST" : "GET", queryString.append("&")
                    .toString(), postString.toString());
            Log.e("+++++", "===signvalue====" + signvalue);
            builderUrl.addQueryParameter("_sign", signvalue);

            Request newRequest = newRequestBuilder.url(builderUrl.build()).build();
            Log.e("+++++", "===newRequest requestUrl====" + builderUrl.build()
                    .toString());

            Response response = chain.proceed(newRequestBuilder.build());
            String responseEncrypt = TextUtils.isEmpty(response.header("Content-Encrypt")) ? response
                    .header("Content_Encrypt") : response.header("Content-Encrypt");// Content-Encrypt="AES/QBSH"
            if (!TextUtils.isEmpty(responseEncrypt) && "AES/QBSH".equals(responseEncrypt)) {
                MediaType mediaType = response.body().contentType();
                try {
                    String responseContent = response.body().string();
                    Log.e("+++++","==responseContent===" + responseContent);
                    responseContent = EncryptUtil.decryptBase64(responseContent);
                    Log.e("+++++","==EncryptUtil responseContent===" + responseContent);
                    return response.newBuilder()
                            .body(ResponseBody.create(mediaType, responseContent))
                            .build();
                } catch (Exception e) {
                    return response.newBuilder()
                            .body(ResponseBody.create(mediaType, ""))
                            .build();
                }
            }
            return response;
        }
    };

    private static Cache getOkHttpCache() {
        File cacheFile = new File(Global.getContext().getCacheDir(), "OkCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        return cache;
    }

}
