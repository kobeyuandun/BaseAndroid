package com.baseandroid.repository.services;

import com.baseandroid.repository.json.CheckUpdate;
import com.baseandroid.repository.json.Data;
import com.baseandroid.repository.json.Result;
import com.baseandroid.repository.json.ServerTime;
import com.baseandroid.repository.json.UserDate;
import com.baseandroid.repository.json.UserTokenInfo;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ConfigService {

    @GET("api/v1/basic/get_server_time")
    Observable<Data<ServerTime>> getServerTime();

    @GET("api/v1/sale/basic/login")
    Observable<Data<UserTokenInfo>> loginApp(@QueryMap Map<String, String> loginMap);

    @GET("api/v1/sale/basic/get_user_info")
    Observable<Data<UserDate>> getUserinfo();

    @FormUrlEncoded
    @POST("api/v1/sale/area/count_up_area")
    Observable<Data<Result>> getNewGroundedCount(@FieldMap Map<String, String> newgrMap);

    @FormUrlEncoded
    @POST("api/v1/basic/get_app_config")
    Observable<Data<CheckUpdate>> checkUpdate(@FieldMap Map<String, String> updateMap);

    @FormUrlEncoded
    @POST("api/v1/sale/visit/add_visit")
    Observable<Data> addVisit(@FieldMap Map<String, String> updateMap);


    /*--------ali------------*/
    @POST("rest/160601/ocr/ocr_idcard.json")
    Observable<String> testOcrIdcard(@Body RequestBody body);

    @POST
    Observable<String> testDriverLicense(@Url String url,@Body RequestBody body);

    @POST
    Observable<String> testShopSign(@Url String url,@Body RequestBody body);

}