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
    public List<DBObject> mTimeSlots;
    public String mHospital;
    public ArrayList mReviews;

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
        mPhoneNumber = "";
        mAddress = "";
        mHospital = "";
        mTimeSlots = new ArrayList<>();
        mReviews = new ArrayList();
    }

    public void setTimeSlots(List<DBObject> ts) { this.mTimeSlots = ts; }
    public List<DBObject> getTimeSlots() {return this.mTimeSlots;}

    public void setmEducation(String mEducation) { this.mEducation = mEducation; }
    public void setmExperience(String mExperience) { this.mExperience = mExperience; }
    public void setmSpecialization(String mSpecialization) { this.mSpecialization = mSpecialization; }
    public void setmHospital(String mHospital) { this.mHospital = mHospital; }
    public void setmPersonalBio(String mPersonalBio) { this.mPersonalBio = mPersonalBio; }
    public void setmReviews(ArrayList mReviews) { this.mReviews = mReviews; }
}
