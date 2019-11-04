package com.infinitycare.health.login.service;

import com.infinitycare.health.database.DoctorRepository;
import com.infinitycare.health.database.IpRepository;
import com.infinitycare.health.database.PatientRepository;
import com.infinitycare.health.login.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ProfileService extends ServiceUtility {

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
        String username = getUsername(request);

        Map<String, Object> result = new HashMap<>();

        if(userType.equals(PATIENT)) {
            Optional<PatientDetails> userQueriedFromDB = patientRepository.findById(Integer.toString(username.hashCode()));
            if(userQueriedFromDB.isPresent()) {
                result.put("username", username);
                result.put("email", userQueriedFromDB.get().mEmail);
                result.put("firstname", userQueriedFromDB.get().mFirstName);
                result.put("lastname", userQueriedFromDB.get().mLastName);
                result.put("address", userQueriedFromDB.get().mAddress);
                result.put("phonenumber", userQueriedFromDB.get().mPhoneNumber);
                result.put("dob", userQueriedFromDB.get().mDOB);
                result.put("insurancecompany", userQueriedFromDB.get().mInsuranceCompany);
                result.put("insuranceprovider", userQueriedFromDB.get().mInsuranceProvider);
                result.put("insuranceplan", userQueriedFromDB.get().mInsurancePlan);
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
                result.put("education", userQueriedFromDB.get().mEducation);
                result.put("hospital", userQueriedFromDB.get().mHospital);
                result.put("specialization", userQueriedFromDB.get().mSpecialization);
                result.put("address", userQueriedFromDB.get().mAddress);
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
                result.put("company", userQueriedFromDB.get().mCompany);
                result.put("phonenumber", userQueriedFromDB.get().mPhoneNumber);
                result.put("address", userQueriedFromDB.get().mAddress);
            }
        }

        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> editDoctorProfile(HttpServletRequest request, String section) {
        String username = getUsername(request);

        Map<String, Object> result = new HashMap<>();
        boolean isProfileUpdated = false;

        Optional<DoctorDetails> userQueriedFromDB = doctorRepository.findById(Integer.toString(username.hashCode()));
        DoctorDetails doctorDetails = userQueriedFromDB.get();

        if(section.equals("hospital")) {

            doctorDetails.setHospital(getPostBodyInAMap(request).get("hospital"));
            doctorDetails.setSpecialization(getPostBodyInAMap(request).get("specialization"));
            doctorDetails.setAddress(getPostBodyInAMap(request).get("address"));
            isProfileUpdated = true;
        }

        if(section.equals("aboutme")) {
            doctorDetails.setPersonalBio(getPostBodyInAMap(request).get("aboutme"));
            isProfileUpdated = true;
        }

        doctorRepository.save(doctorDetails);
        result.put("isProfileUpdated", isProfileUpdated);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> editPatientProfile(HttpServletRequest request, String section) {
        String username = getUsername(request);

        Map<String, Object> result = new HashMap<>();
        boolean isProfileUpdated = false;

        Optional<PatientDetails> userQueriedFromDB = patientRepository.findById(Integer.toString(username.hashCode()));
        PatientDetails patientDetails = userQueriedFromDB.get();

        if(section.equals("personal")) {
            patientDetails.setAddress(getPostBodyInAMap(request).get("address"));
            patientDetails.setPhoneNumber(getPostBodyInAMap(request).get("phonenumber"));
            isProfileUpdated = true;
        }

        if(section.equals("insurance")) {
            patientDetails.setmInsuranceCompany(getPostBodyInAMap(request).get("insurancecompany"));
            patientDetails.setmInsurancePlan(getPostBodyInAMap(request).get("insuranceplan"));
            patientDetails.setmInsuranceProvider(getPostBodyInAMap(request).get("insuranceprovider"));
            Optional<IPDetails> ipFromDB = ipRepository.findById(Integer.toString(getPostBodyInAMap(request).get("insuranceprovider").hashCode()));
            if(ipFromDB.isPresent()) {
                IPDetails ipDetails = ipFromDB.get();
                ArrayList patients = ipFromDB.get().mPatients;
                patients.add(getPostBodyInAMap(request).get("username"));
                ipDetails.setmPatients(patients);
                ipRepository.save(ipDetails);
            }
            isProfileUpdated = true;
        }

        if(section.equals("emergencycontact")) {
            patientDetails.setmEmergencyContactName(getPostBodyInAMap(request).get("emergencycontactname"));
            patientDetails.setmEmergencyContactNumber(getPostBodyInAMap(request).get("emergencycontactnumber"));
            isProfileUpdated = true;
        }

        if(section.equals("medicalhistory")) {
            patientDetails.setmMedicalHistory(getPostBodyInAMap(request).get("medicalhistory"));
            isProfileUpdated = true;
        }

        patientRepository.save(patientDetails);
        result.put("isProfileUpdated", isProfileUpdated);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> editIpProfile(HttpServletRequest request) {
        String username = getUsername(request);

        Map<String, Object> result = new HashMap<>();
        boolean isProfileUpdated = false;

        Optional<IPDetails> userQueriedFromDB = ipRepository.findById(Integer.toString(username.hashCode()));

        if(userQueriedFromDB.isPresent()) {
            IPDetails ipDetails = userQueriedFromDB.get();
            ipDetails.setAddress(getPostBodyInAMap(request).get("address"));
            ipDetails.setPhoneNumber(getPostBodyInAMap(request).get("phonenumber"));
            ipRepository.save(ipDetails);
            isProfileUpdated = true;
        }

        result.put("isProfileUpdated", isProfileUpdated);
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
            result.put("totalrating", doctorQueriedFromDB.get().mTotalRating);
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
