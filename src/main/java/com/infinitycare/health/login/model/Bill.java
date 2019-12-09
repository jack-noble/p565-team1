package com.infinitycare.health.login.model;

public class Bill {

    public static final String IN_PROCESS = "inProcess";
    public static final String APPROVED = "approved";
    public static final String DENIED = "denied";

    private String mDoctorName;
    private String mReason;
    private String mDisplayDate;
    private int mAmountToBePaid;
    private String mAppointmentId;

    public Bill(String doctorName, String reason, String displayDate, int amountToBePaid, String appointmentId) {
        mDoctorName = doctorName;
        mReason = reason;
        mDisplayDate = displayDate;
        mAmountToBePaid = amountToBePaid;
        mAppointmentId = appointmentId;
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

    public String getAppointmentId() {
        return mAppointmentId;
    }

    public void setAppointmentId(String mAppointmentId) {
        this.mAppointmentId = mAppointmentId;
    }

}
