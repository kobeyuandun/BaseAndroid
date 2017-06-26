package com.baseandroid.repository.json;

import android.support.annotation.Keep;


@Keep
public class ServerTime {
    private String current_server_time;

    public String getCurrent_server_time() {
        return current_server_time;
    }

    public void setCurrent_server_time(String current_server_time) {
        this.current_server_time = current_server_time;
    }

    @Override
    public String toString() {
        return "ServerTime{" + "current_server_time='" + current_server_time + '\'' + '}';
    }
}
