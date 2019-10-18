package com.infinitycare.health.login.model;

import com.infinitycare.health.database.DoctorRepository;
import com.infinitycare.health.database.IpRepository;
import com.infinitycare.health.database.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class CookieDetails {

    public static final String SESSIONID = "sessionid";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String PATIENT = "patient";
    public static final String DOCTOR = "doctor";
    public static final String INSURANCE_PROVIDER = "insurance";
    public static final String IS_CREDENTIALS_ACCURATE = "isCredentialsAccurate";
    public static final String IS_OTP_SENT = "isOtpSent";
    public static final String IS_OTP_ACCURATE = "isOtpAccurate";
    public static final String IS_COOKIE_TAMPERED = "isCookieTampered";
    public static final String IS_NEW_USER = "isNewUser";

}
