package com.infinitycare.health.login.service;

import com.infinitycare.health.database.DoctorRepository;
import com.infinitycare.health.database.IpPlanRepository;
import com.infinitycare.health.database.IpRepository;
import com.infinitycare.health.database.PatientRepository;
import com.infinitycare.health.login.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class ProfileService extends CookieDetails {

    @Autowired
    public PatientRepository patientRepository;

    @Autowired
    public IpRepository ipRepository;

    @Autowired
    public DoctorRepository doctorRepository;

    @Autowired
    public IpPlanRepository ipPlanRepository;

    public ProfileService(PatientRepository patientRepository, IpRepository ipRepository, DoctorRepository doctorRepository, IpPlanRepository ipPlanRepository) {
        this.patientRepository = patientRepository;
        this.ipRepository = ipRepository;
        this.doctorRepository = doctorRepository;
        this.ipPlanRepository = ipPlanRepository;
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

    public ResponseEntity<?> getIplans(HttpServletRequest request) {
        String username = request.getParameter("username");
        ArrayList iplans = new ArrayList();
        List<Optional<IpPlanDetails>> iPlans = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();

        Optional<IPDetails> userQueriedFromDB = ipRepository.findById(Integer.toString(username.hashCode()));
        if(userQueriedFromDB.isPresent()) { iplans = userQueriedFromDB.get().mIplans; }

        for (Object iplan : iplans) {
            Optional<IpPlanDetails> planFromDB = ipPlanRepository.findById(Integer.toString(iplan.hashCode()));
            if (planFromDB.isPresent()) { iPlans.add(planFromDB); }
        }

        result.put("Iplans", iPlans);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> editProfile(HttpServletRequest request, String userType) {
        String username = request.getParameter("username");
        String fieldName = request.getParameter("fieldName");
        String fieldValue = request.getParameter("fieldValue");
        Map<String, Object> result = new HashMap<>();
        boolean isFieldUpdated = false;

        if(userType.equals(PATIENT)){
            PatientDetails patientDetails = new PatientDetails(username, "");
            switch (fieldName) {
                case "email":
                    patientDetails.setmEmail(fieldValue); break;
                case "firstname":
                    patientDetails.setmFirstName(fieldValue); break;
                case "lastname":
                    patientDetails.setmLastName(fieldValue); break;
                case "address":
                    patientDetails.setmAddress(fieldValue); break;
                case "phonenumber":
                    patientDetails.setmPhoneNumber(fieldValue); break;
                case "dob":
                    patientDetails.setmDOB(fieldValue); break;
                case "emergencycontactname":
                    patientDetails.setmEmergencyContactName(fieldValue); break;
                case "emergencycontactnumber":
                    patientDetails.setmEmergencyContactNumber(fieldValue); break;
                case "medicalhistory":
                    patientDetails.setmMedicalHistory(fieldValue); break;
                case "insuranceplan":
                    patientDetails.setmInsurancePlan(fieldValue); break;
                case "insuranceprovider":
                    patientDetails.setmInsuranceProvider(fieldValue); break;
                case "insurancecompany":
                    patientDetails.setmInsuranceCompany(fieldValue); break;
            }
            isFieldUpdated = true;
            patientRepository.save(patientDetails);
        }

        if(userType.equals(DOCTOR)) {
            DoctorDetails doctorDetails = new DoctorDetails(username, "");
            switch (fieldName) {
                case "email":
                    doctorDetails.setmEmail(fieldValue); break;
                case "firstname":
                    doctorDetails.setmFirstName(fieldValue); break;
                case "lastname":
                    doctorDetails.setmLastName(fieldValue); break;
                case "address":
                    doctorDetails.setmAddress(fieldValue); break;
                case "phonenumber":
                    doctorDetails.setmPhoneNumber(fieldValue); break;
                case "education":
                    doctorDetails.setmEducation(fieldValue); break;
                case "experience":
                    doctorDetails.setmExperience(fieldValue); break;
                case "specialization":
                    doctorDetails.setmSpecialization(fieldValue); break;
                case "hospital":
                    doctorDetails.setmHospital(fieldValue); break;
                case "biosummary":
                    doctorDetails.setmPersonalBio(fieldValue); break;
            }
            isFieldUpdated = true;
            doctorRepository.save(doctorDetails);
        }

        if(userType.equals(INSURANCE_PROVIDER)) {
            IPDetails ipDetails = new IPDetails(username, "");
            switch (fieldName) {
                case "email":
                    ipDetails.setmEmail(fieldValue); break;
                case "firstname":
                    ipDetails.setmFirstName(fieldValue); break;
                case "lastname":
                    ipDetails.setmLastName(fieldValue); break;
                case "address":
                    ipDetails.setmAddress(fieldValue); break;
                case "phonenumber":
                    ipDetails.setmPhoneNumber(fieldValue); break;
                case "company":
                    ipDetails.setmCompany(fieldValue); break;
            }
            isFieldUpdated = true;
            ipRepository.save(ipDetails);
        }

        result.put("isFieldUpdate", isFieldUpdated);

        return ResponseEntity.ok(result);
    }

}
