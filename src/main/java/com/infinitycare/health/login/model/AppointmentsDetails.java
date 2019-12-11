package com.infinitycare.health.login.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
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
    public String mDisplayDate;
    public String mDisplayTime;
    public String mEncodedDoctorUserName;
    public String mEncodedPatientName;
    public boolean mActive = true;
    public String mPatientName;
    public String mDoctorName;

    private String mReason;

    private String mInsurancePlan;

    private boolean isBillPaidByPatient;
    private String insuranceProviderBillStatus;

    public AppointmentsDetails(String mPatientUsername, String mDoctorUsername, Date mDate, String mHospital,
                               String mLocation, String mPatientName, String mDoctorName) {
        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm");
        this.id = Integer.toString((mPatientUsername + mDoctorUsername + dateFormat.format(mDate)).hashCode());
        this.mPatientUsername = mPatientUsername;
        this.mDoctorUsername = mDoctorUsername;
        this.mEncodedDoctorUserName = Base64.getEncoder().encodeToString(mDoctorUsername.getBytes());
        this.mEncodedPatientName = Base64.getEncoder().encodeToString(mPatientUsername.getBytes());
        this.mHospital = mHospital;
        this.mLocation = mLocation;
        this.mDate = mDate;
        this.mDisplayDate = new SimpleDateFormat("EEE, d MMM yyyy").format(mDate);
        this.mDisplayTime = String.valueOf(mDate.getHours() - 9);
        this.mPatientName = mPatientName;
        this.mDoctorName = mDoctorName;
        this.isBillPaidByPatient = false;
        this.insuranceProviderBillStatus = Bill.IN_PROCESS;
    }

    public String getDoctorDisplayName() {
        return mDoctorName;
    }

    public String getPatientUsername() {
        return mPatientUsername;
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
}
