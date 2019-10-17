package com.infinitycare.health.login.model;

public class UserDetails {

    public String id;
    public String mUserName;
    public String mEmail;
    public String mPassword;
    public String mFirstName;
    public String mLastName;
    public String mMFAToken;

    public String getUserName() {
        return mUserName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        mPassword = mPassword;
    }

    public void setUserName(String mUserName) {
        mUserName = mUserName;
    }

    public String getId() { return id; }

    public void setId(String id) { id = id; }

    public void setMFAToken(String mToken) { mToken = mToken; }

    public String getMFAToken() { return mMFAToken; }

}
