package com.infinitycare.health.login.model;

import com.mongodb.DBObject;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "DoctorDetails")
public class DoctorDetails extends UserDetails {

    public String mEducation;
    public String mExperience;
    public String mSpecialization;
    public String mPersonalBio;
    public ArrayList mTimeSlots;
    public String mHospital;
    public ArrayList mReviews;

    public DoctorDetails(String mUserName) {
        this.mUserName = mUserName;
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
