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
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

// TODO Change Active to True once the OTP during SingUp is authenticated

@Service
public class SignUpService extends CookieDetails {

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

    public ResponseEntity<?> signup(HttpServletRequest request, HttpServletResponse response, String userType) {

        boolean isNewUser = false;
        boolean isOtpSent = false;
        String otp = SendEmailSMTP.generateRandomNumber(1000, 9999);
        Map<String, Object> result = new HashMap<>();
        List<DBObject> timeslots = new ArrayList<>();

        String username = request.getParameter(USERNAME);
        String password = TextSecurer.encrypt(request.getParameter(PASSWORD));

        if(userType.equals(PATIENT)) {
            PatientDetails patientDetails = new PatientDetails(username, password);
            if(!doesPatientAlreadyExist(patientDetails)){
                patientDetails.setMFAToken(otp);
                patientRepository.save(patientDetails);
                isNewUser = true;
            }
        }

        if (userType.equals(DOCTOR)) {
            DoctorDetails doctorDetails = new DoctorDetails(username, password);
            if(!doesDoctorAlreadyExist(doctorDetails)){
                doctorDetails.setMFAToken(otp);
                doctorRepository.save(doctorDetails);
                DBObject ts = new BasicDBObject();
                for (int i = 0; i < 13; i++) {
                    ts.put("isAvailable", true);
                    ts.put("_id", i);
                    ts.put("start", Integer.toString(9 + i) + ":00");
                    ts.put("end", Integer.toString(10 + i) + ":00");
                    timeslots.add(ts);
                }
                isNewUser = true;
            }
        }

        if(userType.equals(INSURANCE_PROVIDER)) {
            IPDetails ipDetails = new IPDetails(username, password);
            if(!doesIpAlreadyExist(ipDetails)){
                ipDetails.setMFAToken(otp);
                ipRepository.save(ipDetails);
                isNewUser = true;
            }
        }

        if(isNewUser) { SendEmailSMTP.sendFromGMail(new String[]{username}, "Please enter the OTP in the signup screen", otp); isOtpSent = true;}
        result.put(IS_NEW_USER, isNewUser);
        result.put(IS_OTP_SENT, isOtpSent);

        SetEncryptedSessionId.setEncryptedSessionId(request, response, username, userType);
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
