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
public class ForgotPasswordService extends ServiceUtility {

    private static final String IS_USER_PRESENT = "isUserPresent";

    @Autowired
    public PatientRepository patientRepository;

    @Autowired
    public DoctorRepository doctorRepository;

    @Autowired
    public IpRepository ipRepository;

    public ForgotPasswordService(PatientRepository patientRepository, DoctorRepository doctorRepository, IpRepository ipRepository){
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.ipRepository = ipRepository;
    }

    public ResponseEntity<?> setPassword(HttpServletRequest request, String userType) {
        boolean isPasswordChanged = false;

        Map<String, Object> result = new HashMap<>();

        Map<String, String> postBody = getPostBodyInAMap(request);

        String username = getUsername(request);
        String password = TextSecurer.encrypt(postBody.get(PASSWORD));

        if(null == username) {
            result.put(IS_COOKIE_TAMPERED, "true");
        } else if(userType.equals(PATIENT)) {
            Optional<PatientDetails> userQueriedFromDB = patientRepository.findById(Integer.toString(username.hashCode()));
            PatientDetails patientDetails = userQueriedFromDB.get();
            patientDetails.setPassword(password);
            patientRepository.save(patientDetails);
            isPasswordChanged = true;
        } else if(userType.equals(DOCTOR)) {
            Optional<DoctorDetails> userQueriedFromDB = doctorRepository.findById(Integer.toString(username.hashCode()));
            DoctorDetails doctorDetails = userQueriedFromDB.get();
            doctorDetails.setPassword(password);
            doctorRepository.save(doctorDetails);
            isPasswordChanged = true;
        } else if(userType.equals(INSURANCE_PROVIDER)) {
            Optional<IPDetails> userQueriedFromDB = ipRepository.findById(Integer.toString(username.hashCode()));
            IPDetails ipDetails = userQueriedFromDB.get();
            ipDetails.setPassword(password);
            ipRepository.save(ipDetails);
            isPasswordChanged = true;
        }

        result.put(IS_PASSWORD_CHANGED, isPasswordChanged);

        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> verifyUsername(HttpServletRequest request, HttpServletResponse response, String userType) {
        boolean isUserPresent = false;
        Map<String, Object> result = new HashMap<>();
        String otp = SendEmailSMTP.generateRandomNumber(1000, 9999);

        String username = getPostBodyInAMap(request).get(EMAIL_ID);

        if(null == username) {
            result.put(IS_COOKIE_TAMPERED, "true");
        } else if(userType.equals(PATIENT)) {
            Optional<PatientDetails> patientDetails = patientRepository.findById(String.valueOf(username.hashCode()));
            if(patientDetails.isPresent()) {
                isUserPresent = true;
                patientDetails.get().setMFAToken(otp);
                patientRepository.save(patientDetails.get());
            }
        } else if(userType.equals(DOCTOR)) {
            Optional<DoctorDetails> doctorDetails = doctorRepository.findById(String.valueOf(username.hashCode()));
            if(doctorDetails.isPresent()) {
                isUserPresent = true;
                doctorDetails.get().setMFAToken(otp);
                doctorRepository.save(doctorDetails.get());
            }
        } else if(userType.equals(INSURANCE_PROVIDER)) {
            Optional<IPDetails> ipDetails = ipRepository.findById(String.valueOf(username.hashCode()));
            if(ipDetails.isPresent()) {
                isUserPresent = true;
                ipDetails.get().setMFAToken(otp);
                ipRepository.save(ipDetails.get());
            }
        }

        result.put(IS_USER_PRESENT, isUserPresent);

        if(isUserPresent) {
            setSessionId(request, response, TextSecurer.encrypt(username), userType, -1);
            sendLoginOTP(username, otp);
        }

        return ResponseEntity.ok(result);
    }

}
