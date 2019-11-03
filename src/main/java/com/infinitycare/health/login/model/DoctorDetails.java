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

    public DoctorDetails(String mUserName) {
        this.mUserName = mUserName;
        this.mDoctorURLInSearch = ServiceUtility.FRONTEND_BASE_URL + "/patient/doctor/" + Base64.getEncoder().encodeToString(mUserName.getBytes());
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
    }

    public void setTimeSlots(ArrayList ts) { this.mTimeSlots = ts; }
    public ArrayList getTimeSlots() {return this.mTimeSlots;}

    public void setmSpecialization(String mSpecialization) { this.mSpecialization = mSpecialization; }
    public void setmHospital(String mHospital) { this.mHospital = mHospital; }
    public void setmPersonalBio(String mPersonalBio) { this.mPersonalBio = mPersonalBio; }
    public void setmReviews(ArrayList mReviews) { this.mReviews = mReviews; }
}
