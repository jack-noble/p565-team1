package com.infinitycare.health.login.service;

import com.infinitycare.health.database.DoctorRepository;
import com.infinitycare.health.database.IpRepository;
import com.infinitycare.health.database.PatientRepository;
import com.infinitycare.health.login.SendEmailSMTP;
import com.infinitycare.health.login.model.DoctorDetails;
import com.infinitycare.health.login.model.IPDetails;
import com.infinitycare.health.login.model.PatientDetails;
import com.infinitycare.health.login.model.ServiceUtility;
import com.infinitycare.health.security.TextSecurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class SignUpService extends ServiceUtility {

    @Autowired
    public PatientRepository patientRepository;

    @Autowired
    public DoctorRepository doctorRepository;

    @Autowired
    public IpRepository ipRepository;

    public SignUpService(PatientRepository patientRepository, DoctorRepository doctorRepository, IpRepository ipRepository){
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.ipRepository = ipRepository;
    }

    public ResponseEntity<?> signUp(HttpServletRequest request, HttpServletResponse response, String userType) {

        boolean isNewUser = false;
        boolean isOtpSent = false;
        String otp = SendEmailSMTP.generateRandomNumber(1000, 9999);
        Map<String, Object> result = new HashMap<>();

        Map<String, String> postBody = getPostBodyInAMap(request);
        String username = postBody.get(EMAIL);

        if(userType.equals(PATIENT)) {
            PatientDetails patientDetails = new PatientDetails(username);
            if(!doesPatientAlreadyExist(patientDetails)){
                patientDetails.setPassword(postBody.get(PASSWORD));
                patientDetails.setEmail(postBody.get(EMAIL));
                patientDetails.setFirstName(postBody.get(FIRSTNAME));
                patientDetails.setLastName(postBody.get(LASTNAME));
                patientDetails.setAddress(postBody.get(ADDRESS));
                patientDetails.setDOB(postBody.get(DOB));
                patientDetails.setPhoneNumber(postBody.get(PHONENUMBER));

                patientDetails.setMFAToken(otp);

                patientRepository.save(patientDetails);
                isNewUser = true;
            }
        }

        if (userType.equals(DOCTOR)) {
            DoctorDetails doctorDetails = new DoctorDetails(username);
            if(!doesDoctorAlreadyExist(doctorDetails)){
                doctorDetails.setPassword(postBody.get(PASSWORD));
                doctorDetails.setEmail(postBody.get(EMAIL));
                doctorDetails.setFirstName(postBody.get(FIRSTNAME));
                doctorDetails.setLastName(postBody.get(LASTNAME));
                doctorDetails.setAddress(postBody.get(ADDRESS));
                doctorDetails.setHospital(postBody.get(HOSPITAL));
                doctorDetails.setSpecialization(SPECIALIZATION);

                doctorDetails.setMFAToken(otp);

                doctorRepository.save(doctorDetails);
                isNewUser = true;
            }
        }

        if(userType.equals(INSURANCE_PROVIDER)) {
            IPDetails ipDetails = new IPDetails(username);
            if(!doesIpAlreadyExist(ipDetails)){
                ipDetails.setPassword(postBody.get(PASSWORD));
                ipDetails.setEmail(postBody.get(EMAIL));
                ipDetails.setFirstName(postBody.get(FIRSTNAME));
                ipDetails.setLastName(postBody.get(LASTNAME));
                ipDetails.setAddress(postBody.get(ADDRESS));
                ipDetails.setCompany(postBody.get(COMPANY));
                ipDetails.setPhoneNumber(postBody.get(PHONENUMBER));

                ipDetails.setMFAToken(otp);

                ipRepository.save(ipDetails);
                isNewUser = true;
            }
        }

        if(isNewUser) { SendEmailSMTP.sendFromGMail(new String[]{username}, "Please enter the OTP in the signup screen", otp); isOtpSent = true;}
        result.put(IS_NEW_USER, isNewUser);
        result.put(IS_OTP_SENT, isOtpSent);

        setSessionId(request, response, TextSecurer.encrypt(username), userType, -1);
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
