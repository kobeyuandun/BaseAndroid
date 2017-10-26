package com.baseandroid.repository.config;

import com.baseandroid.config.Api;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitManager {

    private static Retrofit.Builder buildBaseRetrofit() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder().client(OkHttpClientManager
                .getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson));
        return retrofitBuilder;
    }

    private static Retrofit.Builder buildBaseRetrofit1() {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder().client(OkHttpClientManager
                .getOkHttpClient()).addConverterFactory(ScalarsConverterFactory.create());
        return retrofitBuilder;
    }

    private static Retrofit.Builder buildBaseRetrofitAli() {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder().client(OkHttpClientManagerAli
                .getOkHttpClient()).addConverterFactory(ScalarsConverterFactory.create());
        return retrofitBuilder;
    }

    public static Retrofit getRxRetrofit() {
        return buildBaseRetrofit().baseUrl(Api.sServerApiUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static Retrofit getRxRetrofit1() {
        return buildBaseRetrofit1().baseUrl(Api.sServerApiUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static Retrofit getRxRetrofitAli() {
        return buildBaseRetrofitAli().baseUrl("http://dm-51.data.aliyun.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static Retrofit getRxRetrofitLife() {
        return buildBaseRetrofitAli().baseUrl("http://s.qianbaocard.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
