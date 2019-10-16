package com.infinitycare.health.login.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "DoctorDetails")
public class DoctorDetails {

    private String id;
    private String mUserName;
    private String mEmail;
    private String mPassword;
    private String mFirstName;
    private String mLastName;
    private String mEducation;
    private String mExperience;
    private String mSpecialization;
    private String mPersonalBio;
    private String mToken;

    public DoctorDetails(String mUserName, String mPassword) {
        this.mUserName = mUserName;
        this.mEmail = "";
        this.id = Integer.toString(mUserName.hashCode());
        this.mPassword = mPassword;
        this.mFirstName = "";
        this.mLastName = "";
        this.mEducation = "";
        this.mExperience = "";
        this.mSpecialization = "";
        this.mPersonalBio = "";
        this.mToken = "";
    }

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
        this.mPassword = mPassword;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getmToken() { return mToken; }

    public void setmToken(String mToken) { this.mToken = mToken; }
}
