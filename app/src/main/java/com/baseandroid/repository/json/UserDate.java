package com.baseandroid.repository.json;

public class UserDate {

    private UserInfo user;

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserDate{" + "user=" + user.toString() + '}';
    }
}
