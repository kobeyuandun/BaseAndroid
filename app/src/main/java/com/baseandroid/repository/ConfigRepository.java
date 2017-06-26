package com.baseandroid.repository;

import com.baseandroid.repository.config.RetrofitManager;
import com.baseandroid.repository.json.CheckUpdate;
import com.baseandroid.repository.json.Data;
import com.baseandroid.repository.json.Result;
import com.baseandroid.repository.json.ServerTime;
import com.baseandroid.repository.json.UserDate;
import com.baseandroid.repository.json.UserTokenInfo;
import com.baseandroid.repository.services.ConfigService;

import java.util.Map;

import io.reactivex.Observable;

public class ConfigRepository {

    private volatile static ConfigRepository instance;

    private ConfigRepository() {

    }

    public static ConfigRepository getInstance() {
        if (instance == null) {
            synchronized (ConfigRepository.class) {
                if (instance == null) {
                    instance = new ConfigRepository();
                }
            }
        }
        return instance;
    }

    public Observable<Data<ServerTime>> getServerTime() {
        ConfigService service = RetrofitManager.getRxRetrofit()
                .create(ConfigService.class);
        return service.getServerTime();
    }

    public Observable<Data<UserTokenInfo>> loginApp(Map<String, String> loginMap) {
        ConfigService service = RetrofitManager.getRxRetrofit()
                .create(ConfigService.class);
        return service.loginApp(loginMap);
    }

    public Observable<Data<UserDate>> getUserinfo() {
        ConfigService service = RetrofitManager.getRxRetrofit()
                .create(ConfigService.class);
        return service.getUserinfo();
    }

    public Observable<Data<Result>> getNewGroundedCount(Map<String, String> newgrMap) {
        ConfigService service = RetrofitManager.getRxRetrofit()
                .create(ConfigService.class);
        return service.getNewGroundedCount(newgrMap);
    }

    public Observable<Data<CheckUpdate>> checkUpdate(Map<String, String> updateMap) {
        ConfigService service = RetrofitManager.getRxRetrofit()
                .create(ConfigService.class);
        return service.checkUpdate(updateMap);
    }

    public Observable<Data> addVisit(Map<String, String> visitMap) {
        ConfigService service = RetrofitManager.getRxRetrofit()
                .create(ConfigService.class);
        return service.addVisit(visitMap);
    }

}