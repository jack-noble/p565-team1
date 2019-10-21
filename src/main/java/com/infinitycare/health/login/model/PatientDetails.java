package com.infinitycare.health.login.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PatientDetails")
public class PatientDetails extends UserDetails {

    public String mDOB;
    public String mAddress;
    public String mPhone;
    public String mEmergencyContact;
    public String mInsurancePlanName;
    public String mInsuranceProvider;
    public String mMedicalHistory;

    public PatientDetails(String mUserName, String mPassword) {
        this.mUserName = mUserName;
        mEmail = "";
        id = Integer.toString(mUserName.hashCode());
        this.mPassword = mPassword;
        mFirstName = "";
        mLastName = "";
        mDOB = "";
        mAddress = "";
        mPhone = "";
        mEmergencyContact = "";
        mInsurancePlanName = "";
        mInsuranceProvider = "";
        mMedicalHistory = "";
        mActive = false;
        mMFAToken = "";
    }
}
