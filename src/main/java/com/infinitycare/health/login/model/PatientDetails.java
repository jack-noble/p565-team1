package com.infinitycare.health.login.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PatientDetails")
public class PatientDetails extends UserDetails {

    public String mDOB;
    public String mEmergencyContactName;
    public String mEmergencyContactNumber;
    public String mInsurancePlan;
    public String mInsuranceProvider;
    public String mInsuranceCompany;
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
        mPhoneNumber = "";
        mEmergencyContactName = "";
        mEmergencyContactNumber = "";
        mInsuranceCompany = "";
        mInsurancePlan = "";
        mInsuranceProvider = "";
        mMedicalHistory = "";
        mActive = false;
        mMFAToken = "";
    }
}
