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

    public void setmDOB (String mDOB) { this.mDOB = mDOB; }
    public void setmEmergencyContactName (String mEmergencyContactName) { this.mEmergencyContactName = mEmergencyContactName; }
    public void setmEmergencyContactNumber (String mEmergencyContactNumber) { this.mEmergencyContactNumber = mEmergencyContactNumber; }
    public void setmMedicalHistory (String mMedicalHistory) { this.mMedicalHistory = mMedicalHistory; }
    public void setmInsurancePlan(String mInsurancePlan) { this.mInsurancePlan = mInsurancePlan; }
    public void setmInsuranceProvider(String mInsuranceProvider) { this.mInsuranceProvider = mInsuranceProvider; }
    public void setmInsuranceCompany(String mInsuranceCompany) { this.mInsuranceCompany = mInsuranceCompany; }

}
