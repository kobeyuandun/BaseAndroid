package com.baseandroid.repository;

import com.baseandroid.repository.config.RetrofitManager;
import com.baseandroid.repository.json.CheckUpdate;
import com.baseandroid.repository.json.Data;
import com.baseandroid.repository.json.Result;
import com.baseandroid.repository.json.ServerTime;
import com.baseandroid.repository.json.UserDate;
import com.baseandroid.repository.json.UserTokenInfo;
import com.baseandroid.repository.services.ConfigService;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

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

    public Observable<String> testOcrIdcard(RequestBody body) {
        ConfigService service = RetrofitManager.getRxRetrofitAli()
                .create(ConfigService.class);
        return service.testOcrIdcard(body);
    }

    public Observable<String> testDriverLicense(String url, RequestBody body) {
        ConfigService service = RetrofitManager.getRxRetrofitAli()
                .create(ConfigService.class);
        return service.testDriverLicense(url, body);
    }

    public Observable<String> testShopSign(String url, RequestBody body) {
        ConfigService service = RetrofitManager.getRxRetrofitAli()
                .create(ConfigService.class);
        return service.testShopSign(url, body);
    }

    /* fileupload */
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    private MultipartBody.Part prepareFilePart(String partName, String filepath) {
        File file = new File(filepath);
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }

    public Observable<ResponseBody> uploadFileWithPartMap(Map<String, String> paramMap, String filekey, String filepath) {
        // create part for file (photo, video, ...)
        MultipartBody.Part body = prepareFilePart(filekey, filepath);
        HashMap<String, RequestBody> hashMap = new HashMap<>();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            RequestBody requestBody = createPartFromString(entry.getValue());
            hashMap.put(entry.getKey(), requestBody);
        }

        ConfigService service = RetrofitManager.getRxRetrofitLife()
                .create(ConfigService.class);
        return service.uploadFileWithPartMap(hashMap, body);
    }

}