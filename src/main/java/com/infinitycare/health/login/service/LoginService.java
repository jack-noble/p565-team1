package com.infinitycare.health.login.service;

import com.infinitycare.health.database.DoctorRepository;
import com.infinitycare.health.database.IpRepository;
import com.infinitycare.health.database.PatientRepository;
import com.infinitycare.health.login.SendEmailSMTP;
import com.infinitycare.health.login.model.CookieDetails;
import com.infinitycare.health.login.model.DoctorDetails;
import com.infinitycare.health.login.model.IPDetails;
import com.infinitycare.health.login.model.PatientDetails;
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
public class LoginService extends CookieDetails {

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

        String username = request.getParameter(USERNAME);
        String password = TextSecurer.encrypt(request.getParameter(PASSWORD));

        if(userType.equals(PATIENT)) {
            PatientDetails patientDetails = new PatientDetails(username, password);
            if(checkIfPatientCredentialsAreAccurate(patientDetails)){
                isCredentialsAccurate = true;
                patientDetails.setMFAToken(otp);
                patientRepository.save(patientDetails);
            }
        }

        if(userType.equals(DOCTOR)) {
            DoctorDetails doctorDetails = new DoctorDetails(username, password);
            if(checkIfDoctorCredentialsAreAccurate(doctorDetails)) {
                isCredentialsAccurate = true;
                doctorDetails.setMFAToken(otp);
                doctorRepository.save(doctorDetails);
            }
        }

        if(userType.equals(INSURANCE_PROVIDER)) {
            IPDetails ipDetails = new IPDetails(username, password);
            if(checkIfIpCredentialsAreAccurate(ipDetails)) {
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

        SetEncryptedSessionId.setEncryptedSessionId(request, response, username, userType);
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
