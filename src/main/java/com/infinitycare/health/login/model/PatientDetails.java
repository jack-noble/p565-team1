package com.infinitycare.health.login.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PatientDetails")
public class PatientDetails {

    private String id;
    private String mUserName;
    private String mEmail;
    private String mPassword;
    private String mFirstName;
    private String mLastName;
    private String mDOB;
    private String mAddress;
    private String mPhone;
    private String mEmergencyContact;
    private String mInsurancePlanName;
    private String mInsuranceProvider;
    private String mMedicalHistory;
    private boolean mActive;
    private String mToken;

    public PatientDetails(String mUserName, String mPassword) {
        this.mUserName = mUserName;
        this.mEmail = "";
        this.id = Integer.toString(mUserName.hashCode());
        this.mPassword = mPassword;
        this.mFirstName = "";
        this.mLastName = "";
        this.mDOB = "";
        this.mAddress = "";
        this.mPhone = "";
        this.mEmergencyContact = "";
        this.mInsurancePlanName = "";
        this.mInsuranceProvider = "";
        this.mMedicalHistory = "";
        this.mActive = false;
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

    // public String getmTypeOfUser() { return mTypeOfUser; }

    // public void setmTypeOfUser(String mTypeOfUser) { this.mTypeOfUser = mTypeOfUser; }

    public String getmToken() { return mToken; }

    public void setmToken(String mToken) { this.mToken = mToken; }
}
