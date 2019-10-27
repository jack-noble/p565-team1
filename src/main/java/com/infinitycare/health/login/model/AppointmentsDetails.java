package com.infinitycare.health.login.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Document(collection = "AppointmentsDetails")
public class AppointmentsDetails{

    @Id
    public String id;
    public String mPatientUsername;
    public String mDoctorUsername;
    public String mHospital;
    public String mLocation;
    public Date mDate;
    public boolean mActive = true;

    public AppointmentsDetails(String mPatientUsername, String mDoctorUsername, String mHospital, String mLocation, Date mDate) {
        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm");
        this.id = Integer.toString((mPatientUsername + mDoctorUsername + dateFormat.format(mDate)).hashCode());
        this.mPatientUsername = mPatientUsername;
        this.mDoctorUsername = mDoctorUsername;
        this.mHospital = mHospital;
        this.mLocation = mLocation;
        this.mDate = mDate;
    }

    public void setStatus(boolean mActive) {
        this.mActive = mActive;
    }

    public boolean getStatus() {
        return this.mActive;
    }

    public String getId() {
        return this.id;
    }

    public Date getDate() {
        return this.mDate;
    }
}
