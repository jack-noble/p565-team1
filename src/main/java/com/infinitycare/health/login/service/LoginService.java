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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LoginService extends ServiceUtility {

    @Autowired
    public PatientRepository patientRepository;

    @Autowired
    public DoctorRepository doctorRepository;

    @Autowired
    public IpRepository ipRepository;

    public LoginService(PatientRepository patientRepository, DoctorRepository doctorRepository, IpRepository ipRepository){
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.ipRepository = ipRepository;
    }

    public ResponseEntity<?> validateCredentials(HttpServletRequest request, HttpServletResponse response, String userType) {
        boolean isCredentialsAccurate = false;
        boolean sentOtp = false;

        String otp = SendEmailSMTP.generateRandomNumber(1000, 9999);
        Map<String, Object> result = new HashMap<>();

        Map<String, String> postBody = getPostBodyInAMap(request);
        String username = postBody.get(USERNAME);
        String password = TextSecurer.encrypt(postBody.get(PASSWORD));

        String finalPassword = password;

        if(userType.equals(PATIENT)) {
            PatientDetails patientDetails = new PatientDetails(username, password);
            Optional<PatientDetails> userQueriedFromDB = patientRepository.findById(Integer.toString(username.hashCode()));
            boolean verifyCredentails = userQueriedFromDB.map(details -> details.getPassword().equals(finalPassword)).orElse(false);
            if(verifyCredentails && !userQueriedFromDB.get().mActive){
                patientDetails.setmActive(true);
                isCredentialsAccurate = true;
                patientDetails.setMFAToken(otp);
                patientRepository.save(patientDetails);
            }
        }

        if(userType.equals(DOCTOR)) {
            DoctorDetails doctorDetails = new DoctorDetails(username, password);
            Optional<DoctorDetails> userQueriedFromDB = doctorRepository.findById(Integer.toString(username.hashCode()));
            boolean verifyCredentails = userQueriedFromDB.map(details -> details.getPassword().equals(finalPassword)).orElse(false);
            if(verifyCredentails && !userQueriedFromDB.get().mActive) {
                doctorDetails.setmActive(true);
                isCredentialsAccurate = true;
                doctorDetails.setMFAToken(otp);
                doctorRepository.save(doctorDetails);
            }
        }

        if(userType.equals(INSURANCE_PROVIDER)) {
            IPDetails ipDetails = new IPDetails(username, password);
            Optional<IPDetails> userQueriedFromDB = ipRepository.findById(Integer.toString(username.hashCode()));
            boolean verifyCredentails = userQueriedFromDB.map(details -> details.getPassword().equals(finalPassword)).orElse(false);
            if(verifyCredentails && !userQueriedFromDB.get().mActive) {
                ipDetails.setmActive(true);
                isCredentialsAccurate = true;
                ipDetails.setMFAToken(otp);
                ipRepository.save(ipDetails);
            }
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
}
