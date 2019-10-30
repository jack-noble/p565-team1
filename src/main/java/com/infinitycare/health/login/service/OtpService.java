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
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class OtpService extends CookieDetails {

    @Autowired
    public PatientRepository patientRepository;

    @Autowired
    public DoctorRepository doctorRepository;

    @Autowired
    public IpRepository ipRepository;

    public OtpService(PatientRepository patientRepository, DoctorRepository doctorRepository, IpRepository ipRepository){
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.ipRepository = ipRepository;
    }

    public ResponseEntity<?> validateOtp(HttpServletRequest request, String userType, String enteredOtp) {
        boolean isOtpAccurate = false;
        String userOtpFromDB = "";
        Map<String, Object> result = new HashMap<>();
        result.put(IS_OTP_ACCURATE, isOtpAccurate);

        // String username = getUsername(request);
        String username = request.getParameter("username");

        if(null == username) {
            result.put(IS_COOKIE_TAMPERED, "true");
        } else if(userType.equals(PATIENT)) {
            Optional<PatientDetails> userFromDB = patientRepository.findById(Integer.toString(username.hashCode()));
            userOtpFromDB = userFromDB.isPresent() ? userFromDB.get().getMFAToken() : "";
        } else if(userType.equals(DOCTOR)) {
            Optional<DoctorDetails> userFromDB = doctorRepository.findById(Integer.toString(username.hashCode()));
            userOtpFromDB = userFromDB.isPresent() ? userFromDB.get().getMFAToken() : "";
        } else if(userType.equals(INSURANCE_PROVIDER)) {
            Optional<IPDetails> userFromDB = ipRepository.findById(Integer.toString(username.hashCode()));
            userOtpFromDB = userFromDB.isPresent() ? userFromDB.get().getMFAToken() : "";
        }

        if(StringUtils.isEmpty(userOtpFromDB)) {
            result.put(IS_COOKIE_TAMPERED, "true");
        } else if(userOtpFromDB.equals(enteredOtp)) {
            isOtpAccurate = true;
            result.put(IS_OTP_ACCURATE, isOtpAccurate);
        }

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
}
