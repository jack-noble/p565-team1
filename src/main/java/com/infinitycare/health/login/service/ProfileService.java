package com.infinitycare.health.login.service;

import com.infinitycare.health.database.DoctorRepository;
import com.infinitycare.health.database.IpRepository;
import com.infinitycare.health.database.PatientRepository;
import com.infinitycare.health.login.model.CookieDetails;
import com.infinitycare.health.login.model.DoctorDetails;
import com.infinitycare.health.login.model.IPDetails;
import com.infinitycare.health.login.model.PatientDetails;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ProfileService extends CookieDetails {

    @Autowired
    public PatientRepository patientRepository;

    @Autowired
    public IpRepository ipRepository;

    @Autowired
    public DoctorRepository doctorRepository;

    public ProfileService(PatientRepository patientRepository, IpRepository ipRepository, DoctorRepository doctorRepository) {
        this.patientRepository = patientRepository;
        this.ipRepository = ipRepository;
        this.doctorRepository = doctorRepository;
    }

    public ResponseEntity<?> getProfile(HttpServletRequest request, String userType) {
        String username = request.getParameter("username");
        Map<String, Object> result = new HashMap<>();

        if(userType.equals(PATIENT)) {
            Optional<PatientDetails> userQueriedFromDB = patientRepository.findById(Integer.toString(username.hashCode()));
            if(userQueriedFromDB.isPresent()) {
                result.put("username", username);
                result.put("email", userQueriedFromDB.get().mEmail);
                result.put("firstname", userQueriedFromDB.get().mFirstName);
                result.put("lastname", userQueriedFromDB.get().mLastName);
                result.put("phonenumber", userQueriedFromDB.get().mPhoneNumber);
                result.put("address", userQueriedFromDB.get().mAddress);
                result.put("insurancecompany", userQueriedFromDB.get().mInsuranceCompany);
                result.put("insuranceprovider", userQueriedFromDB.get().mInsuranceProvider);
                result.put("insuranceplan", userQueriedFromDB.get().mInsurancePlan);
                result.put("dob", userQueriedFromDB.get().mDOB);
                result.put("emergencycontactname", userQueriedFromDB.get().mEmergencyContactName);
                result.put("emergencycontactnumber", userQueriedFromDB.get().mEmergencyContactNumber);
                result.put("medicalhistory", userQueriedFromDB.get().mMedicalHistory);

                Optional<IPDetails> ipFromDB = ipRepository.findById(Integer.toString(userQueriedFromDB.get().mInsuranceProvider.hashCode()));
                ipFromDB.ifPresent(ipDetails -> result.put("insuranceprovidername", (ipDetails.mFirstName + " " + ipDetails.mLastName)));
            }
        }

        if(userType.equals(DOCTOR)) {
            Optional<DoctorDetails> userQueriedFromDB = doctorRepository.findById(Integer.toString(username.hashCode()));
            if(userQueriedFromDB.isPresent()) {
                result.put("username", username);
                result.put("email", userQueriedFromDB.get().mEmail);
                result.put("firstname", userQueriedFromDB.get().mFirstName);
                result.put("lastname", userQueriedFromDB.get().mLastName);
                result.put("phonenumber", userQueriedFromDB.get().mPhoneNumber);
                result.put("address", userQueriedFromDB.get().mAddress);
                result.put("hospital", userQueriedFromDB.get().mHospital);
                result.put("specialization", userQueriedFromDB.get().mSpecialization);
                result.put("education", userQueriedFromDB.get().mEducation);
                result.put("biosummary", userQueriedFromDB.get().mPersonalBio);
            }
        }

        if(userType.equals(INSURANCE_PROVIDER)) {
            Optional<IPDetails> userQueriedFromDB = ipRepository.findById(Integer.toString(username.hashCode()));
            if(userQueriedFromDB.isPresent()) {
                result.put("username", username);
                result.put("email", userQueriedFromDB.get().mEmail);
                result.put("firstname", userQueriedFromDB.get().mFirstName);
                result.put("lastname", userQueriedFromDB.get().mLastName);
                result.put("phonenumber", userQueriedFromDB.get().mPhoneNumber);
                result.put("address", userQueriedFromDB.get().mAddress);
                result.put("company", userQueriedFromDB.get().mCompany);
            }
        }

        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> getDoctorFromPatient(String doctorusername) {
        Map<String, Object> result = new HashMap<>();

        Optional<DoctorDetails> doctorQueriedFromDB = doctorRepository.findById(Integer.toString(doctorusername.hashCode()));
        if(doctorQueriedFromDB.isPresent()) {
            result.put("name", doctorQueriedFromDB.get().mFirstName + " " + doctorQueriedFromDB.get().mLastName);
            result.put("phonenumber", doctorQueriedFromDB.get().mPhoneNumber);
            result.put("hospital", doctorQueriedFromDB.get().mHospital);
            result.put("address", doctorQueriedFromDB.get().mAddress);
            result.put("specialization", doctorQueriedFromDB.get().mSpecialization);
            result.put("education", doctorQueriedFromDB.get().mEducation);
            result.put("biosummary", doctorQueriedFromDB.get().mPersonalBio);
            result.put("reviews", doctorQueriedFromDB.get().mReviews);
        }

        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> getPatientFromOthers(String patientusername) {
        Map<String, Object> result = new HashMap<>();

        Optional<PatientDetails> patientQueriedFromDB = patientRepository.findById(Integer.toString(patientusername.hashCode()));
        if(patientQueriedFromDB.isPresent()) {
            result.put("name", patientQueriedFromDB.get().mFirstName + " " + patientQueriedFromDB.get().mLastName);
            result.put("address", patientQueriedFromDB.get().mAddress);
            result.put("phonenumber", patientQueriedFromDB.get().mPhoneNumber);
            result.put("insurancecompany", patientQueriedFromDB.get().mInsuranceCompany);
            result.put("insuranceplan", patientQueriedFromDB.get().mInsurancePlan);
            result.put("emergencycontact", patientQueriedFromDB.get().mEmergencyContactName + " " + patientQueriedFromDB.get().mEmergencyContactNumber);
            result.put("medicalhistory", patientQueriedFromDB.get().mMedicalHistory);
            result.put("dob", patientQueriedFromDB.get().mDOB);
        }

        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> getIpFromPatient(String ipusername) {
        Map<String, Object> result = new HashMap<>();

        Optional<IPDetails> ipFromDB = ipRepository.findById(Integer.toString(ipusername.hashCode()));
        if(ipFromDB.isPresent()) {
            result.put("name", ipFromDB.get().mFirstName + " " + ipFromDB.get().mLastName);
            result.put("company", ipFromDB.get().mCompany);
            result.put("address", ipFromDB.get().mAddress);
            result.put("phonenumber", ipFromDB.get().mPhoneNumber);
        }

        return ResponseEntity.ok(result);
    }
}
