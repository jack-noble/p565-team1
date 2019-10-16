package com.infinitycare.health.login.controller;

import com.infinitycare.health.database.IpRepository;
import com.infinitycare.health.database.PatientRepository;
import com.infinitycare.health.login.SendEmailSMTP;
import com.infinitycare.health.login.model.DoctorDetails;
import com.infinitycare.health.login.model.IPDetails;
import com.infinitycare.health.login.model.PatientDetails;
import com.infinitycare.health.database.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

// TODO create an abstract class for the 3 user types instead of 6 extra methods!

@Controller
public class LoginController {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    IpRepository ipRepository;

    String retainedPatientUsername;
    String retainedDoctorUsername;
    String retainedIpUsername;

    @RequestMapping("/")
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("login.html");
        return mv;
    }

    @RequestMapping(value = "/LoggedIn/{userType}")
    public ModelAndView test(HttpServletRequest request, HttpServletResponse response, @PathVariable String userType) {
        ModelAndView mv = new ModelAndView();
        boolean isCredentialAccurate = false;

        if(userType.equals("patient")) {
            PatientDetails patientDetails = new PatientDetails(request.getParameter("username"), request.getParameter("password"));
            retainedPatientUsername = retainPatientUsername(patientDetails);
            isCredentialAccurate = checkIfPatientCredentialsAreAccurate(patientDetails);
            String otp = SendEmailSMTP.generateRandomNumber(1000, 9999);
            patientDetails.setmToken(otp);
            patientRepository.save(patientDetails);
            SendEmailSMTP.sendFromGMail(new String[]{request.getParameter("username")}, "Please enter the OTP in the login screen", otp);
        }

        if(userType.equals("doctor")) {
            DoctorDetails doctorDetails = new DoctorDetails(request.getParameter("username"), request.getParameter("password"));
            retainedDoctorUsername = retainDoctorUsername(doctorDetails);
            isCredentialAccurate = checkIfDoctorCredentialsAreAccurate(doctorDetails);
            String otp = SendEmailSMTP.generateRandomNumber(1000, 9999);
            doctorDetails.setmToken(otp);
            doctorRepository.save(doctorDetails);
            SendEmailSMTP.sendFromGMail(new String[]{request.getParameter("username")}, "Please enter the OTP in the login screen", otp);
        }

        if(userType.equals("insurance")) {
            IPDetails ipDetails = new IPDetails(request.getParameter("username"), request.getParameter("password"));
            retainedIpUsername = retainIpUsername(ipDetails);
            isCredentialAccurate = checkIfIpCredentialsAreAccurate(ipDetails);
            String otp = SendEmailSMTP.generateRandomNumber(1000, 9999);
            ipDetails.setmToken(otp);
            ipRepository.save(ipDetails);
            SendEmailSMTP.sendFromGMail(new String[]{request.getParameter("username")}, "Please enter the OTP in the login screen", otp);
        }

        if(!isCredentialAccurate) {
            response.setHeader("Location", "/");
            response.setStatus(303);
            mv.setViewName("login.html");
            return mv;
        }

        mv.setViewName("LoggedIn.html");
        //Check the username and password against the data in Database
        //Should use an encrypted password while matching against the rows in a DB. Would be ideal if we are able to send an encrypted password
        return mv;
    }

    @RequestMapping(value = "/otp/{userType}", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView testOtp(HttpServletResponse response, @PathVariable String userType, @RequestParam("otp") String enteredOtp) {
        ModelAndView mv = new ModelAndView();
        boolean isOtpAccurate = false;
        String userOtpFromDB = "";

        if(userType.equals("patient")) {
            String retainedUsername = retainedPatientUsername;

            Optional<PatientDetails> userFromDB = patientRepository.findById(Integer.toString(retainedUsername.hashCode()));
            PatientDetails userDetails = userFromDB.get();
            userOtpFromDB = userDetails.getmToken();

            if(userOtpFromDB.equals(enteredOtp))
            {
                isOtpAccurate = true;
            }
        }

        if(userType.equals("doctor")) {
            String retainedUsername = retainedDoctorUsername;

            Optional<DoctorDetails> userFromDB = doctorRepository.findById(Integer.toString(retainedUsername.hashCode()));
            DoctorDetails userDetails = userFromDB.get();
            userOtpFromDB = userDetails.getmToken();

            System.out.println("OTP from DB: " + userOtpFromDB);
            if(userOtpFromDB.equals(enteredOtp))
            {
                isOtpAccurate = true;
            }
        }

        if(userType.equals("insurance")) {
            String retainedUsername = retainedIpUsername;

            Optional<IPDetails> userFromDB = ipRepository.findById(Integer.toString(retainedUsername.hashCode()));
            IPDetails userDetails = userFromDB.get();
            userOtpFromDB = userDetails.getmToken();

            System.out.println("OTP from DB: " + userOtpFromDB);
            if(userOtpFromDB.equals(enteredOtp))
            {
                isOtpAccurate = true;
            }
        }

        if(!isOtpAccurate) {
            response.setHeader("Location", "/");
            response.setStatus(303);
            mv.setViewName("login.html");
            return mv;
        }

        mv.setViewName("Welcome.html");
        //Check the username and password against the data in Database
        //Should use an encrypted password while matching against the rows in a DB. Would be ideal if we are able to send an encrypted password
        return mv;
    }

    @RequestMapping(value = "/signin/{userType}")
    @ResponseBody
    public String login(HttpServletRequest request, @PathVariable String userType) {

        boolean isAccurateCredentials = false;

        if(userType.equals("patient")) {
            PatientDetails patientDetails = new PatientDetails(request.getParameter("username"), request.getParameter("password"));
            isAccurateCredentials = checkIfPatientCredentialsAreAccurate(patientDetails);
        }

        System.out.println("Signing in");

        if(!isAccurateCredentials)
            return "isCredentialsCorrect:" + isAccurateCredentials;

        String otp = SendEmailSMTP.generateRandomNumber(1000, 9999);
        SendEmailSMTP.sendFromGMail(new String[]{request.getParameter("username")}, "Please enter the OTP in the login screen", otp);

        //Check the username and password against the data in Database
        //Should use an encrypted password while matching against the rows in a DB. Would be ideal if we are able to send an encrypted password
        return "isCredentialsCorrect:" + isAccurateCredentials;
    }

    // To retain username between the /login page and the /otp page
    private static String retainPatientUsername(PatientDetails patientDetails) {

        return patientDetails.getUserName();
    }

    private static String retainDoctorUsername(DoctorDetails doctorDetails) {

        return doctorDetails.getUserName();
    }

    private static String retainIpUsername(IPDetails ipDetails) {

        return ipDetails.getUserName();
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
