package com.infinitycare.health.login.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "IPDetails")
public class IPDetails extends UserDetails {

    public String mCompany;
    public ArrayList mIplans;

    public IPDetails(String mUserName, String mPassword) {
        this.mUserName = mUserName;
        mEmail = "";
        id = Integer.toString(mUserName.hashCode());
        this.mPassword = mPassword;
        mFirstName = "";
        mLastName = "";
        mCompany = "";
        mActive = false;
        mPhoneNumber = "";
        mAddress = "";
        mMFAToken = "";
        mIplans = new ArrayList();
    }
}
