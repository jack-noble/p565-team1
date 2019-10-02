package com.infinitycare.health.login.model;

public class UserDetails {

    private String mUserName;
    private String mPassword;

    public String getUserName() {
        return mUserName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }
}
