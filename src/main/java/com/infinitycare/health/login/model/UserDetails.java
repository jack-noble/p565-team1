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
    public String getPassword() { return mPassword; }

    public String getMFAToken() { return mMFAToken; }
    public boolean getmActive() { return mActive; }

    public void setId(String id) { this.id = id; }
    public void setUserName(String mUserName) { this.mUserName = mUserName; }
    public void setPassword(String mPassword) { this.mPassword = mPassword; }
    public void setEmail(String mEmail) { this.mEmail = mEmail; }
    public void setFirstName(String mFirstName) { this.mFirstName = mFirstName; }
    public void setLastName(String mLastName) { this.mLastName = mLastName; }
    public void setActive(boolean mActive) { this.mActive = mActive; }

    public void setMFAToken(String mMFAToken) { this.mMFAToken = mMFAToken; }
    public void setPhoneNumber(String mPhoneNumber) { this.mPhoneNumber = mPhoneNumber; }
    public void setAddress(String mAddress) { this.mAddress = mAddress; }
    public String getAddress() { return mAddress; }
}
