package com.infinitycare.health.login.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "DoctorDetails")
public class DoctorDetails extends UserDetails {

    private String mEducation;
    private String mExperience;
    private String mSpecialization;
    private String mPersonalBio;
    private boolean mActive;

    public DoctorDetails(String mUserName, String mPassword) {
        mUserName = mUserName;
        mEmail = "";
        id = Integer.toString(mUserName.hashCode());
        mPassword = mPassword;
        mFirstName = "";
        mLastName = "";
        mEducation = "";
        mExperience = "";
        mSpecialization = "";
        mPersonalBio = "";
        mActive = false;
        mMFAToken = "";
    }

}
