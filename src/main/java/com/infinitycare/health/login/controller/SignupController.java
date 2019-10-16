package com.infinitycare.health.login.controller;

import com.infinitycare.health.database.DoctorRepository;
import com.infinitycare.health.database.IpRepository;
import com.infinitycare.health.login.SendEmailSMTP;
import com.infinitycare.health.login.model.DoctorDetails;
import com.infinitycare.health.login.model.IPDetails;
import com.infinitycare.health.login.model.PatientDetails;
import com.infinitycare.health.database.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class SignupController {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    IpRepository ipRepository;

    @RequestMapping(value = "/signup/{userType}")
    @ResponseBody
    public String signup(HttpServletRequest request, @PathVariable String userType) {

        String msg = "";
        if(userType.equals("patient")) {
            PatientDetails patientDetails = new PatientDetails(request.getParameter("username"), request.getParameter("password"));
            if(doesPatientAlreadyExist(patientDetails)){
                System.out.println("Error: this account already exists, log in or reset your password");
                // TODO - display to front end, if method returns var account_already_exists display error
                msg = "account_already_exists";
            }
            else{
                patientRepository.save(patientDetails);
                System.out.println("Signing up");
                SendEmailSMTP.sendFromGMail(new String[]{request.getParameter("username")}, "Please enter the OTP in the signup screen", SendEmailSMTP.generateRandomNumber(1000, 9999));
            }
        }

        if (userType.equals("doctor")) {
            DoctorDetails doctorDetails = new DoctorDetails(request.getParameter("username"), request.getParameter("password"));
            if(doesDoctorAlreadyExist(doctorDetails)){
                System.out.println("Error: this account already exists, log in or reset your password");
                // TODO - display to front end, if method returns var account_already_exists display error
                msg = "account_already_exists";
            }
            else{
                doctorRepository.save(doctorDetails);
                System.out.println("Signing up");
                SendEmailSMTP.sendFromGMail(new String[]{request.getParameter("username")}, "Please enter the OTP in the signup screen", SendEmailSMTP.generateRandomNumber(1000, 9999));
            }
        }

        if(userType.equals("insurance")) {
            IPDetails ipDetails = new IPDetails(request.getParameter("username"), request.getParameter("password"));
            if(doesIpAlreadyExist(ipDetails)){
                System.out.println("Error: this account already exists, log in or reset your password");
                // TODO - display to front end, if method returns var account_already_exists display error
                msg = "account_already_exists";
            }
            else{
                ipRepository.save(ipDetails);
                System.out.println("Signing up");
                SendEmailSMTP.sendFromGMail(new String[]{request.getParameter("username")}, "Please enter the OTP in the signup screen", SendEmailSMTP.generateRandomNumber(1000, 9999));
            }
        }
        return msg;
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

}
