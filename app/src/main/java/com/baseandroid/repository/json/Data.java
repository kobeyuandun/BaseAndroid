package com.baseandroid.repository.json;

import android.support.annotation.Keep;

@Keep
public class Data<T> {

    private String message;
    private String total_time;
    private T result;
    private String status;
    private String srv_time;
    private String info;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTotal_time() {
        return total_time;
    }

    public void setTotal_time(String total_time) {
        this.total_time = total_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSrv_time() {
        return srv_time;
    }

    public void setSrv_time(String srv_time) {
        this.srv_time = srv_time;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
