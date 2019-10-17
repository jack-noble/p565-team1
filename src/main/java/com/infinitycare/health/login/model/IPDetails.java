package com.infinitycare.health.login.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "IPDetails")
public class IPDetails extends UserDetails {

    private String mCompany;

    public IPDetails(String aUserName, String aPassword) {
        mUserName = aUserName;
        mEmail = "";
        id = Integer.toString(mUserName.hashCode());
        mPassword = aPassword;
        mFirstName = "";
        mLastName = "";
        mCompany = "";
        mMFAToken = "";
    }

}
