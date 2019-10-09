package com.infinitycare.health.login.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "UserDetails")
public class UserDetails {

    @Id
    private ObjectId id;
    private String mUserName;
    private String mPassword;
    private String mTypeOfUser;
    private String mToken;

    public UserDetails(String mUserName, String mPassword, String mTypeOfUser) {
        this.id = ObjectId.get();
        this.mUserName = mUserName;
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

    public ObjectId getId() { return id; }

    public void setId(ObjectId id) { this.id = id; }

    public String getmTypeOfUser() { return mTypeOfUser; }

    public void setmTypeOfUser(String mTypeOfUser) { this.mTypeOfUser = mTypeOfUser; }

    public String getmToken() { return mToken; }

    public void setmToken(String mToken) { this.mToken = mToken; }
}
