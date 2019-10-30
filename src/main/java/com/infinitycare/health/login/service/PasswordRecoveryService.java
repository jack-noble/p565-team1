package com.infinitycare.health.login.service;

import com.infinitycare.health.database.DoctorRepository;
import com.infinitycare.health.database.IpRepository;
import com.infinitycare.health.database.PatientRepository;
import com.infinitycare.health.login.model.CookieDetails;
import com.infinitycare.health.login.model.DoctorDetails;
import com.infinitycare.health.login.model.IPDetails;
import com.infinitycare.health.login.model.PatientDetails;
import com.infinitycare.health.security.TextSecurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PasswordRecoveryService extends CookieDetails {

    @Autowired
    public PatientRepository patientRepository;

    @Autowired
    public DoctorRepository doctorRepository;

    @Autowired
    public IpRepository ipRepository;

    public PasswordRecoveryService(PatientRepository patientRepository, DoctorRepository doctorRepository, IpRepository ipRepository){
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.ipRepository = ipRepository;
    }

    public ResponseEntity<?> setPassword(HttpServletRequest request, String userType) {
        boolean ispasswordChanged = false;

        Map<String, Object> result = new HashMap<>();

        // String username = request.getParameter(USERNAME);
        String password = TextSecurer.encrypt(request.getParameter(PASSWORD));

        if(null == username) {
            result.put(IS_COOKIE_TAMPERED, "true");
        } else if(userType.equals(PATIENT)) {
            PatientDetails patientDetails = new PatientDetails(username, password);
            patientDetails.setmPassword(password);
            patientRepository.save(patientDetails);
            ispasswordChanged = true;
        } else if(userType.equals(DOCTOR)) {
            DoctorDetails doctorDetails = new DoctorDetails(username, password);
            doctorDetails.setmPassword(password);
            doctorRepository.save(doctorDetails);
            ispasswordChanged = true;
        } else if(userType.equals(INSURANCE_PROVIDER)) {
            IPDetails ipDetails = new IPDetails(username, password);
            ipDetails.setmPassword(password);
            ipRepository.save(ipDetails);
            ispasswordChanged = true;
        }

        result.put(IS_PASSWORD_CHANGED, ispasswordChanged);

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
