package com.infinitycare.health.login.model;

import com.mongodb.DBObject;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "DoctorDetails")
public class DoctorDetails extends UserDetails {

    public String mEducation;
    public String mExperience;
    public String mSpecialization;
    public String mPersonalBio;
    public List<DBObject> mTimeSlots;

    public DoctorDetails(String mUserName, String mPassword) {
        this.mUserName = mUserName;
        mEmail = "";
        id = Integer.toString(mUserName.hashCode());
        this.mPassword = mPassword;
        mFirstName = "";
        mLastName = "";
        mEducation = "";
        mExperience = "";
        mSpecialization = "";
        mPersonalBio = "";
        mActive = false;
        mMFAToken = "";
        mTimeSlots = new ArrayList<>();
    }

    public void setTimeSlots(List<DBObject> ts) { this.mTimeSlots = ts; }

    public List<DBObject> getTimeSlots() {return this.mTimeSlots;}
}
