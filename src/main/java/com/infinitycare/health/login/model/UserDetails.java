package com.infinitycare.health.login.model;

import org.springframework.data.annotation.Id;

public class UserDetails {

    @Id
    public String id;
    public String mUserName;
    public String mEmail;
    public String mPassword;
    public String mFirstName;
    public String mLastName;
    public boolean mActive;
    public String mMFAToken;
    public String mPhoneNumber;
    public String mAddress;

    public String getUserName() { return mUserName; }

    public String getEmail() { return mEmail; }

    public String getPassword() { return mPassword; }

    public void setPassword(String mPassword) { mPassword = mPassword; }

    public void setUserName(String mUserName) { mUserName = mUserName; }

    public String getId() { return id; }

    public void setId(String id) { id = id; }

    public void setMFAToken(String mMFAToken) { this.mMFAToken = mMFAToken; }

    public String getMFAToken() { return mMFAToken; }

    public void setmActive(boolean mActive) { mActive = mActive; }

    public boolean getmActive() { return mActive; }

}
