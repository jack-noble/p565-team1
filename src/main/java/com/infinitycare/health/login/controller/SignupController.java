package com.infinitycare.health.login.controller;

import com.infinitycare.health.database.DoctorRepository;
import com.infinitycare.health.database.IpRepository;
import com.infinitycare.health.login.SendEmailSMTP;
import com.infinitycare.health.login.model.DoctorDetails;
import com.infinitycare.health.login.model.IPDetails;
import com.infinitycare.health.login.model.PatientDetails;
import com.infinitycare.health.database.PatientRepository;
import com.infinitycare.health.security.TextSecurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// TODO Add /otp method for this SignUpController
// TODO Refractor all the methods into service folder and declare @Service classes to reuse code

/* @Controller
public class SignupController {

    public static final String SESSIONID = "sessionid";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String PATIENT = "patient";
    public static final String DOCTOR = "doctor";
    public static final String INSURANCE_PROVIDER = "insurance";
    public static final String IS_NEW_USER = "isNewUser";
    public static final String IS_OTP_SENT = "isOtpSent";
    public static final String IS_OTP_ACCURATE = "isOtpAccurate";
    public static final String IS_COOKIE_TAMPERED = "isCookieTampered";

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    IpRepository ipRepository;

    @RequestMapping(value = "/signup/{userType}")
    @ResponseBody
    public ResponseEntity<?> signup(HttpServletRequest request, HttpServletResponse response, @PathVariable String userType) {

        boolean isNewUser = false;
        boolean isOtpSent = false;
        String otp = SendEmailSMTP.generateRandomNumber(1000, 9999);
        Map<String, Object> result = new HashMap<>();

        String username = request.getParameter(USERNAME);
        String password = request.getParameter(PASSWORD);

        if(userType.equals(PATIENT)) {
            PatientDetails patientDetails = new PatientDetails(username, password);
            if(!doesPatientAlreadyExist(patientDetails)){
                patientDetails.setMFAToken(otp);
                patientRepository.save(patientDetails);
                isNewUser = true;
            }
        }

        if (userType.equals(DOCTOR)) {
            DoctorDetails doctorDetails = new DoctorDetails(username, password);
            if(!doesDoctorAlreadyExist(doctorDetails)){
                doctorDetails.setMFAToken(otp);
                doctorRepository.save(doctorDetails);
                isNewUser = true;
            }
        }

        if(userType.equals(INSURANCE_PROVIDER)) {
            IPDetails ipDetails = new IPDetails(username, password);
            if(!doesIpAlreadyExist(ipDetails)){
                ipDetails.setMFAToken(otp);
                ipRepository.save(ipDetails);
                isNewUser = true;
            }
        }

        if(isNewUser) { SendEmailSMTP.sendFromGMail(new String[]{username}, "Please enter the OTP in the signup screen", otp); isOtpSent = true;}
        result.put(IS_NEW_USER, isNewUser);
        result.put(IS_OTP_SENT, isOtpSent);

        setEncryptedSessionId(request, response, username, userType);
        return ResponseEntity.ok(result);
    }

    private void setEncryptedSessionId(HttpServletRequest request, HttpServletResponse response, String username, String userType) {
        String encryptedSessionId = TextSecurer.encrypt(username);
        String servletPath = request.getServletPath();
        String cookiePath = servletPath.substring(0, servletPath.indexOf(userType) + userType.length());

        Cookie cookie = new Cookie(SESSIONID, encryptedSessionId);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(-1);
        cookie.setPath(cookiePath);

        response.addCookie(cookie);
    }

    private boolean doesPatientAlreadyExist(PatientDetails patientDetails){

        String enteredUsername = patientDetails.getUserName();
        Optional<PatientDetails> userQueriedFromDB = patientRepository.findById(Integer.toString(enteredUsername.hashCode()));

        // returns if user is present
        return userQueriedFromDB.isPresent();
    }

    private boolean doesDoctorAlreadyExist(DoctorDetails doctorDetails){

        String enteredUsername = doctorDetails.getUserName();
        Optional<DoctorDetails> userQueriedFromDB = doctorRepository.findById(Integer.toString(enteredUsername.hashCode()));

        // returns if user is present
        return userQueriedFromDB.isPresent();
    }

    private boolean doesIpAlreadyExist(IPDetails ipDetails){

        String enteredUsername = ipDetails.getUserName();
        Optional<IPDetails> userQueriedFromDB = ipRepository.findById(Integer.toString(enteredUsername.hashCode()));

        // returns if user is present
        return userQueriedFromDB.isPresent();
    }
} */