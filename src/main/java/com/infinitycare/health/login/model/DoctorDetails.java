package com.infinitycare.health.login.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Base64;

@Document(collection = "DoctorDetails")
public class DoctorDetails extends UserDetails {

    public String mEducation;
    public String mExperience;
    public String mSpecialization;
    public String mPersonalBio;
    public ArrayList mTimeSlots;
    public String mHospital;
    public ArrayList mReviews;
    public String mDoctorURLInSearch;

    public float mTotalRating;

    public DoctorDetails(String mUserName) {
        this.mUserName = mUserName;
        this.mDoctorURLInSearch = "/patient/doctor/" + Base64.getEncoder().encodeToString(mUserName.getBytes());
        mEmail = "";
        id = Integer.toString(mUserName.hashCode());
        mPassword = "";
        mFirstName = "";
        mLastName = "";
        mEducation = "";
        mExperience = "";
        mSpecialization = "";
        mPersonalBio = "";
        mActive = false;
        mMFAToken = "";
        mPhoneNumber = "";
        mAddress = "";
        mHospital = "";
        mTimeSlots = new ArrayList<>();
        mReviews = new ArrayList();
        mTotalRating = 0;
    }

    @Override
    public boolean equals(Object o) {
        return this.mUserName.equals(((DoctorDetails)o).getUserName());
    }

    @Override
    public int hashCode() {
        return this.mUserName.hashCode();
    }

    public void setTimeSlots(ArrayList ts) { this.mTimeSlots = ts; }
    public ArrayList getTimeSlots() {return this.mTimeSlots;}

    public void setSpecialization(String mSpecialization) { this.mSpecialization = mSpecialization; }
    public void setHospital(String mHospital) { this.mHospital = mHospital; }
    public void setPersonalBio(String mPersonalBio) { this.mPersonalBio = mPersonalBio; }
    public void setReviews(ArrayList mReviews) { this.mReviews = mReviews; }
    public void setTotalRating(float mTotalRating) { this.mTotalRating = mTotalRating; }
    public void setEducation(String education) { this.mEducation = education; }
}
