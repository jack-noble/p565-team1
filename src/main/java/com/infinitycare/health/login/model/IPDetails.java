package com.infinitycare.health.login.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "IPDetails")
public class IPDetails extends UserDetails {

    private String mCompany;

    public IPDetails(String mUserName, String mPassword) {
        this.mUserName = mUserName;
        mEmail = "";
        id = Integer.toString(mUserName.hashCode());
        this.mPassword = mPassword;
        mFirstName = "";
        mLastName = "";
        mCompany = "";
        mMFAToken = "";
    }

}
