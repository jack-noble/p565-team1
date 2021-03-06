package com.infinitycare.health.login.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "IPDetails")
public class IPDetails extends UserDetails {

    public String mCompany;
    public ArrayList mIplans;
    public ArrayList mPatients;

    public IPDetails(String mUserName) {
        this.mUserName = mUserName;
        mEmail = "";
        id = Integer.toString(mUserName.hashCode());
        mPassword = "";
        mFirstName = "";
        mLastName = "";
        mCompany = "";
        mActive = false;
        mPhoneNumber = "";
        mAddress = "";
        mMFAToken = "";
        mIplans = new ArrayList();
        mPatients = new ArrayList();
    }

    @Override
    public boolean equals(Object o) {
        return this.mUserName.equals(((IPDetails)o).getUserName());
    }

    @Override
    public int hashCode() {
        return this.mUserName.hashCode();
    }

    public void setmPatients (ArrayList mPatients) { this.mPatients = mPatients; }
    public void setmIplans(ArrayList mIplans) { this.mIplans = mIplans; }

    public void setCompany(String company) { this.mCompany = company; };

    public String getCompany() {
        return mCompany;
    }

    public List<String> getIpPlans() {
        return mIplans;
    }
}
