package com.baseandroid.repository;

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

    /*public Observable<Data<Definition>> getDefinition() {
        com.yooopig.speed.repository.services.ConfigService service = RetrofitManager.getRxRetrofit2().create(com.yooopig.speed.repository.services.ConfigService.class);
        return service.getDefinition();
    }*/

}