package com.infinitycare.health.login.service;

import com.infinitycare.health.database.DoctorRepository;
import com.infinitycare.health.database.IpRepository;
import com.infinitycare.health.database.PatientRepository;
import com.infinitycare.health.login.model.DoctorDetails;
import com.infinitycare.health.login.model.IPDetails;
import com.infinitycare.health.login.model.PatientDetails;
import com.infinitycare.health.login.model.ServiceUtility;
import com.infinitycare.health.security.TextSecurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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

        String username = postBody.get(USERNAME);
        String password = TextSecurer.encrypt(postBody.get(PASSWORD));

        if(null == username) {
            result.put(IS_COOKIE_TAMPERED, "true");
        } else if(userType.equals(PATIENT)) {
            PatientDetails patientDetails = new PatientDetails(username);
            patientDetails.setPassword(password);
            patientRepository.save(patientDetails);
            isPasswordChanged = true;
        } else if(userType.equals(DOCTOR)) {
            DoctorDetails doctorDetails = new DoctorDetails(username);
            doctorDetails.setPassword(password);
            doctorRepository.save(doctorDetails);
            isPasswordChanged = true;
        } else if(userType.equals(INSURANCE_PROVIDER)) {
            IPDetails ipDetails = new IPDetails(username);
            ipDetails.setPassword(password);
            ipRepository.save(ipDetails);
            isPasswordChanged = true;
        }

        result.put(IS_PASSWORD_CHANGED, isPasswordChanged);

        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> verifyUsername(HttpServletRequest request, String userType) {
        boolean isUserPresent = false;
        Map<String, Object> result = new HashMap<>();

        String username = getPostBodyInAMap(request).get(EMAIL_ID);

        if(null == username) {
            result.put(IS_COOKIE_TAMPERED, "true");
        } else if(userType.equals(PATIENT)) {
            isUserPresent = patientRepository.existsById(String.valueOf(username.hashCode()));
        } else if(userType.equals(DOCTOR)) {
            isUserPresent = doctorRepository.existsById(String.valueOf(username.hashCode()));
        } else if(userType.equals(INSURANCE_PROVIDER)) {
            isUserPresent = ipRepository.existsById(String.valueOf(username.hashCode()));
        }

        result.put(IS_USER_PRESENT, isUserPresent);

        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> validateUser(HttpServletRequest request, String userType) {

        Map<String, Object> result = new HashMap<>();
        boolean isValidUser = false;
        String username = request.getParameter("username");

        if(userType.equals(PATIENT)) {
            Optional<PatientDetails> userFromDB = patientRepository.findById(Integer.toString(username.hashCode()));
            isValidUser = userFromDB.isPresent();
        }
        if(userType.equals(DOCTOR)) {
            Optional<DoctorDetails> userFromDB = doctorRepository.findById(Integer.toString(username.hashCode()));
            isValidUser = userFromDB.isPresent();
        }
        if(userType.equals(INSURANCE_PROVIDER)) {
            Optional<IPDetails> userFromDB = ipRepository.findById(Integer.toString(username.hashCode()));
            isValidUser = userFromDB.isPresent();
        }

        result.put("isValidUser", isValidUser);
        return ResponseEntity.ok(result);
    }

}
