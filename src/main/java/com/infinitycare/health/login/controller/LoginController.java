package com.infinitycare.health.login.controller;

import com.infinitycare.health.database.IpRepository;
import com.infinitycare.health.database.PatientRepository;
import com.infinitycare.health.login.SendEmailSMTP;
import com.infinitycare.health.login.model.DoctorDetails;
import com.infinitycare.health.login.model.IPDetails;
import com.infinitycare.health.login.model.PatientDetails;
import com.infinitycare.health.database.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    IpRepository ipRepository;

    @RequestMapping("/")
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("login.html");
        return mv;
    }

    @GetMapping(value = "/LoggedIn/{userType}")
    public ResponseEntity<?> validateCredentails(HttpServletRequest request, @PathVariable String userType) {
        boolean isCredentialsAccurate = false;
        boolean sentOtp = false;
        String otp = SendEmailSMTP.generateRandomNumber(1000, 9999);
        Map<String, Object> result = new HashMap<>();
        result.put("isCredentailsAccurate", isCredentialsAccurate);
        result.put("sentOtp", sentOtp);

        if(userType.equals("patient")) {
            PatientDetails patientDetails = new PatientDetails(request.getParameter("username"), request.getParameter("password"));
            isCredentialsAccurate = checkIfPatientCredentialsAreAccurate(patientDetails);
            patientDetails.setmToken(otp);
            patientRepository.save(patientDetails);
        }

        if(userType.equals("doctor")) {
            DoctorDetails doctorDetails = new DoctorDetails(request.getParameter("username"), request.getParameter("password"));
            isCredentialsAccurate = checkIfDoctorCredentialsAreAccurate(doctorDetails);
            doctorDetails.setmToken(otp);
            doctorRepository.save(doctorDetails);
        }

        if(userType.equals("insurance")) {
            IPDetails ipDetails = new IPDetails(request.getParameter("username"), request.getParameter("password"));
            isCredentialsAccurate = checkIfIpCredentialsAreAccurate(ipDetails);
            ipDetails.setmToken(otp);
            ipRepository.save(ipDetails);
        }

        SendEmailSMTP.sendFromGMail(new String[]{request.getParameter("username")}, "Please enter the OTP in the login screen", otp);
        sentOtp = true;
        result.put("isCredentailsAccurate", isCredentialsAccurate);
        result.put("sentOtp", sentOtp);

        return ResponseEntity.ok(result);
        //Should use an encrypted password while matching against the rows in a DB. Would be ideal if we are able to send an encrypted password
    }

    @GetMapping(value = "/otp/{userType}")
    public ResponseEntity<?> validateOtp(HttpServletRequest request, @PathVariable String userType, @RequestParam("otp") String enteredOtp) {
        boolean isOtpAccurate = false;
        String userOtpFromDB = "";
        Map<String, Object> result = new HashMap<>();
        result.put("isOtpAccurate", isOtpAccurate);

        if(userType.equals("patient")) {
            PatientDetails patientDetails = new PatientDetails(request.getParameter("username"), request.getParameter("password"));

            Optional<PatientDetails> userFromDB = patientRepository.findById(Integer.toString(patientDetails.getUserName().hashCode()));
            PatientDetails userDetails = userFromDB.get();
            userOtpFromDB = userDetails.getmToken();
        }

        if(userType.equals("doctor")) {
            DoctorDetails doctorDetails = new DoctorDetails(request.getParameter("username"), request.getParameter("password"));

            Optional<DoctorDetails> userFromDB = doctorRepository.findById(Integer.toString(doctorDetails.getUserName().hashCode()));
            DoctorDetails userDetails = userFromDB.get();
            userOtpFromDB = userDetails.getmToken();
        }

        if(userType.equals("insurance")) {
            IPDetails ipDetails = new IPDetails(request.getParameter("username"), request.getParameter("password"));

            Optional<IPDetails> userFromDB = ipRepository.findById(Integer.toString(ipDetails.getUserName().hashCode()));
            IPDetails userDetails = userFromDB.get();
            userOtpFromDB = userDetails.getmToken();
        }

        if(userOtpFromDB.equals(enteredOtp))
        {
            isOtpAccurate = true;
            result.put("isOtpAccurate", isOtpAccurate);
        }

        return ResponseEntity.ok(result);
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

}

