package com.infinitycare.health.login.service;

import com.infinitycare.health.database.DoctorRepository;
import com.infinitycare.health.database.IpRepository;
import com.infinitycare.health.database.PatientRepository;
import com.infinitycare.health.login.model.DoctorDetails;
import com.infinitycare.health.login.model.IPDetails;
import com.infinitycare.health.login.model.PatientDetails;
import com.infinitycare.health.login.model.ServiceUtility;
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
        String username = request.getParameter("username");
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
        String username = request.getParameter("username");
        Map<String, Object> result = new HashMap<>();
        boolean isProfileUpdated = false;

        DoctorDetails doctorDetails = new DoctorDetails(username, "");

        if(section.equals("hospital")) {
            doctorDetails.setmHospital(request.getParameter("hospital"));
            doctorDetails.setmSpecialization(request.getParameter("specialization"));
            doctorDetails.setmAddress(request.getParameter("address"));
        }

        if(section.equals("aboutme")) {
            doctorDetails.setmPersonalBio(request.getParameter("aboutme"));
        }

        doctorRepository.save(doctorDetails);
        result.put("isProfileUpdated", isProfileUpdated);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> editPatientProfile(HttpServletRequest request, String section) {
        Map<String, Object> result = new HashMap<>();
        boolean isProfileUpdated = false;

        PatientDetails patientDetails = new PatientDetails(request.getParameter("username"), "");

        if(section.equals("personal")) {
            patientDetails.setmAddress(request.getParameter("address"));
            patientDetails.setmPhoneNumber(request.getParameter("phonenumber"));
            isProfileUpdated = true;
        }

        if(section.equals("insurance")) {
            patientDetails.setmInsuranceCompany(request.getParameter("insurancecompany"));
            patientDetails.setmInsurancePlan(request.getParameter("insuranceplan"));
            patientDetails.setmInsuranceProvider(request.getParameter("insuranceprovider"));
            Optional<IPDetails> ipFromDB = ipRepository.findById(Integer.toString(request.getParameter("insuranceprovider").hashCode()));
            IPDetails ipDetails = new IPDetails(request.getParameter("insuranceprovider"), "");
            if(ipFromDB.isPresent()) {
                ArrayList patients = ipFromDB.get().mPatients;
                patients.add(request.getParameter("username"));
                ipDetails.setmPatients(patients);
                ipRepository.save(ipDetails);
            }
            isProfileUpdated = true;
        }

        if(section.equals("emergencycontact")) {
            patientDetails.setmEmergencyContactName(request.getParameter("emergencycontactname"));
            patientDetails.setmEmergencyContactNumber(request.getParameter("emergencycontactnumber"));
            isProfileUpdated = true;
        }

        if(section.equals("medicalhistory")) {
            patientDetails.setmMedicalHistory(request.getParameter("medicalhistory"));
            isProfileUpdated = true;
        }

        patientRepository.save(patientDetails);
        result.put("isProfileUpdated", isProfileUpdated);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> editIpProfile(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        boolean isProfileUpdated = false;

        IPDetails ipDetails = new IPDetails(request.getParameter("username"), "");

        ipDetails.setmAddress(request.getParameter("address"));
        ipDetails.setmPhoneNumber(request.getParameter("phonenumber"));
        isProfileUpdated = true;
        ipRepository.save(ipDetails);

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
