package com.infinitycare.health.login.controller;

import com.infinitycare.health.database.IpRepository;
import com.infinitycare.health.database.PatientRepository;
import com.infinitycare.health.login.SendEmailSMTP;
import com.infinitycare.health.login.model.DoctorDetails;
import com.infinitycare.health.login.model.IPDetails;
import com.infinitycare.health.login.model.PatientDetails;
import com.infinitycare.health.database.DoctorRepository;
import com.infinitycare.health.security.TextSecurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/* @Controller
public class LoginController {

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

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    IpRepository ipRepository;

    @GetMapping(value = "/LoggedIn/{userType}")
    public ResponseEntity<?> validateCredentials(HttpServletRequest request, HttpServletResponse response, @PathVariable String userType) {
        boolean isCredentialsAccurate = false;
        boolean sentOtp = false;

        String otp = SendEmailSMTP.generateRandomNumber(1000, 9999);
        Map<String, Object> result = new HashMap<>();

        String username = request.getParameter(USERNAME);
        String password = request.getParameter(PASSWORD);

        if(userType.equals(PATIENT)) {
            PatientDetails patientDetails = new PatientDetails(username, password);
            isCredentialsAccurate = checkIfPatientCredentialsAreAccurate(patientDetails);
            patientDetails.setMFAToken(otp);
            patientRepository.save(patientDetails);
        }

        if(userType.equals(DOCTOR)) {
            DoctorDetails doctorDetails = new DoctorDetails(username, password);
            isCredentialsAccurate = checkIfDoctorCredentialsAreAccurate(doctorDetails);
            doctorDetails.setMFAToken(otp);
            doctorRepository.save(doctorDetails);
        }

        if(userType.equals(INSURANCE_PROVIDER)) {
            IPDetails ipDetails = new IPDetails(username, password);
            isCredentialsAccurate = checkIfIpCredentialsAreAccurate(ipDetails);
            ipDetails.setMFAToken(otp);
            ipRepository.save(ipDetails);
        }

        if(isCredentialsAccurate) {
            SendEmailSMTP.sendFromGMail(new String[]{username}, "Please enter the OTP in the login screen", otp);
            sentOtp = true;
        }

        result.put(IS_CREDENTIALS_ACCURATE, isCredentialsAccurate);
        result.put(IS_OTP_SENT, sentOtp);

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

    @GetMapping(value = "/otp/{userType}")
    public ResponseEntity<?> validateOtp(HttpServletRequest request, @PathVariable String userType, @RequestParam("otp") String enteredOtp) {
        boolean isOtpAccurate = false;
        String userOtpFromDB = "";
        Map<String, Object> result = new HashMap<>();
        result.put(IS_OTP_ACCURATE, isOtpAccurate);

        String username = getUsername(request);

        if(null == username) {
            result.put(IS_COOKIE_TAMPERED, "true");
        } else if(userType.equals(PATIENT)) {
            Optional<PatientDetails> userFromDB = patientRepository.findById(Integer.toString(username.hashCode()));
            userOtpFromDB = userFromDB.isPresent() ? userFromDB.get().getMFAToken() : "";
        } else if(userType.equals(DOCTOR)) {
            Optional<DoctorDetails> userFromDB = doctorRepository.findById(Integer.toString(username.hashCode()));
            userOtpFromDB = userFromDB.isPresent() ? userFromDB.get().getMFAToken() : "";
        } else if(userType.equals(INSURANCE_PROVIDER)) {
            Optional<IPDetails> userFromDB = ipRepository.findById(Integer.toString(username.hashCode()));
            userOtpFromDB = userFromDB.isPresent() ? userFromDB.get().getMFAToken() : "";
        }

        if(StringUtils.isEmpty(userOtpFromDB)) {
            result.put(IS_COOKIE_TAMPERED, "true");
        } else if(userOtpFromDB.equals(enteredOtp)) {
            isOtpAccurate = true;
            result.put(IS_OTP_ACCURATE, isOtpAccurate);
        }

        return ResponseEntity.ok(result);
    }

    private String getUsername(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies) {
            if(SESSIONID.equals(c.getName())) {
                return TextSecurer.decrypt(c.getValue());
            }
        }

        return null;
    }

    private boolean checkIfPatientCredentialsAreAccurate(PatientDetails patientDetails) {
        String enteredUsername = patientDetails.getUserName();
        String enteredPassword = patientDetails.getPassword();

        Optional<PatientDetails> userQueriedFromDB = patientRepository.findById(Integer.toString(enteredUsername.hashCode()));

        // user not found in database
        return userQueriedFromDB.map(details -> details.getPassword().equals(enteredPassword)).orElse(false);
    }

    private boolean checkIfDoctorCredentialsAreAccurate(DoctorDetails doctorDetails) {
        String enteredUsername = doctorDetails.getUserName();
        String enteredPassword = doctorDetails.getPassword();

        Optional<DoctorDetails> userQueriedFromDB = doctorRepository.findById(Integer.toString(enteredUsername.hashCode()));

        // user not found in database
        return userQueriedFromDB.map(details -> details.getPassword().equals(enteredPassword)).orElse(false);
    }

    private boolean checkIfIpCredentialsAreAccurate(IPDetails ipDetails) {
        String enteredUsername = ipDetails.getUserName();
        String enteredPassword = ipDetails.getPassword();

        Optional<IPDetails> userQueriedFromDB = ipRepository.findById(Integer.toString(enteredUsername.hashCode()));

        // user not found in database
        return userQueriedFromDB.map(details -> details.getPassword().equals(enteredPassword)).orElse(false);
    }

} */

