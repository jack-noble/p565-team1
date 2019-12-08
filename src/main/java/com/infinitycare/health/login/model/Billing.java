package com.infinitycare.health.login.model;

public class Billing {

    private String mDoctorName;
    private String mReason;
    private String mDisplayDate;
    private int mAmountToBePaid;

    public Billing(String doctorName, String reason, String displayDate, int amountToBePaid) {
        mDoctorName = doctorName;
        mReason = reason;
        mDisplayDate = displayDate;
        mAmountToBePaid = amountToBePaid;
    }

    public String getDoctorName() {
        return mDoctorName;
    }

    public void setDoctorName(String doctorName) {
        this.mDoctorName = doctorName;
    }

    public String getReason() {
        return mReason;
    }

    public void setReason(String reason) {
        this.mReason = reason;
    }

    public String getDisplayDate() {
        return mDisplayDate;
    }

    public void setDisplayDate(String displayDate) {
        this.mDisplayDate = displayDate;
    }

    public int getAmountToBePaid() {
        return mAmountToBePaid;
    }

    public void setAmountToBePaid(int amountToBePaid) {
        this.mAmountToBePaid = amountToBePaid;
    }

}
