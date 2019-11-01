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
    public void setmPassword(String mPassword) { this.mPassword = mPassword; }
    public void setMFAToken(String mMFAToken) { this.mMFAToken = mMFAToken; }
    public String getMFAToken() { return mMFAToken; }
    public void setmActive(boolean mActive) { this.mActive = mActive; }
    public boolean getmActive() { return mActive; }

    public void setmPhoneNumber(String mPhoneNumber) { this.mPhoneNumber = mPhoneNumber; }
    public void setmAddress(String mAddress) { this.mAddress = mAddress; }
}
