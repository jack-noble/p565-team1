package com.infinitycare.health.login.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PatientDetails")
public class PatientDetails extends UserDetails {

    public String mDOB;
    public String mInsurancePlan;
    public String mInsuranceProvider;
    public String mInsuranceCompany;

    private String mBloodType;
    private String mAllergies;
    private String mCurrentMedications;
    private String mVaccinations;

    public String mMedicalHistory;

    public String mEmergencyContactName;
    public String mEmergencyContactNumber;


    public PatientDetails(String mUserName) {
        this.mUserName = mUserName;
        mEmail = "";
        id = Integer.toString(mUserName.hashCode());
        mPassword = "";
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
        mBloodType = "";
        mAllergies = "";
        mCurrentMedications = "";
        mVaccinations = "";

    }

    @Override
    public boolean equals(Object o) {
        return this.mUserName.equals(((PatientDetails)o).getUserName());
    }

    @Override
    public int hashCode() {
        return this.mUserName.hashCode();
    }

    public void setmEmergencyContactName (String mEmergencyContactName) { this.mEmergencyContactName = mEmergencyContactName; }
    public void setmEmergencyContactNumber (String mEmergencyContactNumber) { this.mEmergencyContactNumber = mEmergencyContactNumber; }
    public void setmMedicalHistory (String mMedicalHistory) { this.mMedicalHistory = mMedicalHistory; }
    public void setmInsurancePlan(String mInsurancePlan) { this.mInsurancePlan = mInsurancePlan; }
    public void setmInsuranceProvider(String mInsuranceProvider) { this.mInsuranceProvider = mInsuranceProvider; }
    public void setmInsuranceCompany(String mInsuranceCompany) { this.mInsuranceCompany = mInsuranceCompany; }
    public void setDOB(String dob) { this.mDOB = dob; }

    public String getBloodType() {
        return mBloodType;
    }

    public void setBloodType(String mBloodType) {
        this.mBloodType = mBloodType;
    }

    public String getAllergies() {
        return mAllergies;
    }

    public void setAllergies(String mAllergies) {
        this.mAllergies = mAllergies;
    }

    public String getCurrentMedications() {
        return mCurrentMedications;
    }

    public void setCurrentMedications(String mCurrentMedications) {
        this.mCurrentMedications = mCurrentMedications;
    }

    public String getVaccinations() {
        return mVaccinations;
    }

    public void setVaccinations(String mVaccinations) {
        this.mVaccinations = mVaccinations;
    }
}
