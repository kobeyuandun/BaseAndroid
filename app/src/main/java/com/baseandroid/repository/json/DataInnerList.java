package com.baseandroid.repository.json;

import android.support.annotation.Keep;

import java.util.List;

@Keep
public class DataInnerList<T> {
    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
