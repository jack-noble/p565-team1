package com.infinitycare.health.login.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "UserDetails")
public class UserDetails {

    private String id;
    private String mUserName;
    private String mPassword;
    private String mTypeOfUser;
    private String mToken;

    public UserDetails(String mUserName, String mPassword, String mTypeOfUser) {
        this.mUserName = mUserName;
        this.id = Integer.toString(mUserName.hashCode());
        this.mPassword = mPassword;
        this.mTypeOfUser = mTypeOfUser;
        this.mToken = "";
    }

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

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getmTypeOfUser() { return mTypeOfUser; }

    public void setmTypeOfUser(String mTypeOfUser) { this.mTypeOfUser = mTypeOfUser; }

    public String getmToken() { return mToken; }

    public void setmToken(String mToken) { this.mToken = mToken; }
}
