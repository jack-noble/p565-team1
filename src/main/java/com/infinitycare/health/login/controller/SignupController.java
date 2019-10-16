package com.infinitycare.health.login.controller;

import com.infinitycare.health.database.DoctorRepository;
import com.infinitycare.health.database.IpRepository;
import com.infinitycare.health.login.SendEmailSMTP;
import com.infinitycare.health.login.model.DoctorDetails;
import com.infinitycare.health.login.model.IPDetails;
import com.infinitycare.health.login.model.PatientDetails;
import com.infinitycare.health.database.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// TODO Add /otp method for this SignUpController
// TODO Refractor all the methods into service folder and declare @Service classes to reuse code

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
    public ResponseEntity<?> signup(HttpServletRequest request, @PathVariable String userType) {

        boolean isNewUser = false;
        String otp = SendEmailSMTP.generateRandomNumber(1000, 9999);
        Map<String, Object> result = new HashMap<>();

        if(userType.equals("patient")) {
            PatientDetails patientDetails = new PatientDetails(request.getParameter("username"), request.getParameter("password"));
            if(!doesPatientAlreadyExist(patientDetails)){
                patientDetails.setmToken(otp);
                patientRepository.save(patientDetails);
                isNewUser = true;
            }
        }

        if (userType.equals("doctor")) {
            DoctorDetails doctorDetails = new DoctorDetails(request.getParameter("username"), request.getParameter("password"));
            if(!doesDoctorAlreadyExist(doctorDetails)){
                doctorDetails.setmToken(otp);
                doctorRepository.save(doctorDetails);
                isNewUser = true;
            }
        }

        if(userType.equals("insurance")) {
            IPDetails ipDetails = new IPDetails(request.getParameter("username"), request.getParameter("password"));
            if(!doesIpAlreadyExist(ipDetails)){
                ipDetails.setmToken(otp);
                ipRepository.save(ipDetails);
                isNewUser = true;
            }
        }

        if(isNewUser) { SendEmailSMTP.sendFromGMail(new String[]{request.getParameter("username")}, "Please enter the OTP in the signup screen", otp); }
        result.put("isNewUser", isNewUser);
        return ResponseEntity.ok(result);
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
