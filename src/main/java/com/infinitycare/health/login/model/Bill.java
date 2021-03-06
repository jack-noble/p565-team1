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
    private String mPatientName;
    private String mPatientUsername;
    private String mDoctorUsername;

    public Bill(String doctorName, String doctorUsername, String reason, String displayDate, int amountToBePaid, String appointmentId, String patientName, String patientUsername) {
        mDoctorName = doctorName;
        mReason = reason;
        mDisplayDate = displayDate;
        mAmountToBePaid = amountToBePaid;
        mAppointmentId = appointmentId;
        mPatientName = patientName;
        mPatientUsername = patientUsername;
        mDoctorUsername = doctorUsername;
    }

    public String getDoctorName() {
        return mDoctorName;
    }

    public void setDoctorName(String doctorName) {
        this.mDoctorName = doctorName;
    }

    public String getDoctorUsername() {
        return mDoctorUsername;
    }

    public void setDoctorUsername(String mDoctorUsername) {
        this.mDoctorUsername = mDoctorUsername;
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

    public String getPatientName() {
        return mPatientName;
    }

    public void setPatientName(String mPatientName) {
        this.mPatientName = mPatientName;
    }

    public String getPatientUsername() {
        return mPatientUsername;
    }

    public void setPatientUsername(String mPatientUsername) {
        this.mPatientUsername = mPatientUsername;
    }

}
