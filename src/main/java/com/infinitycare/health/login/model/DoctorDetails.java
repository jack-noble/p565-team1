package com.infinitycare.health.login.model;

import com.infinitycare.health.security.TextSecurer;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "DoctorDetails")
public class DoctorDetails extends UserDetails {

    private String mEducation;
    private String mExperience;
    private String mSpecialization;
    private String mPersonalBio;

    public DoctorDetails(String mUserName, String mPassword) {
        this.mUserName = mUserName;
        mEmail = "";
        id = Integer.toString(mUserName.hashCode());
        this.mPassword = TextSecurer.encrypt(mPassword);
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
