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

    public String getPatientName() {
        return mPatientName;
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
<<<<<<< Updated upstream
=======

    public void setDisplayTime(String mDisplayTime) {
        this.mDisplayTime = mDisplayTime;
    }

    public String getDisplayTime() {
        return this.mDisplayTime;
    }

    public void formatDisplayDate(Date date) {
        mDisplayDate = new SimpleDateFormat("EEE, d MMM yyyy").format(date);
    }

    public String getDisplayDate() {
        return mDisplayDate;
    }

    public String getDoctorUsername() {
        return mDoctorUsername;
    }

    public String getPatientUsername() {
        return mPatientUsername;
    }

    public String getReason() {
        return mReason;
    }

    public void setReason(String mReason) {
        this.mReason = mReason;
    }

    public String getInsurancePlan() {
        return mInsurancePlan;
    }

    public void setInsurancePlan(String mInsurancePlan) {
        this.mInsurancePlan = mInsurancePlan;
    }

    public boolean isBillPaidByPatient() {
        return isBillPaidByPatient;
    }

    public void setBillPaidByPatient(boolean billPaidByPatient) {
        isBillPaidByPatient = billPaidByPatient;
    }

    public String getInsuranceProviderBillStatus() {
        return insuranceProviderBillStatus;
    }

    public void setInsuranceProviderBillStatus(String billStatus) {
        insuranceProviderBillStatus = billStatus;
    }
>>>>>>> Stashed changes
}
