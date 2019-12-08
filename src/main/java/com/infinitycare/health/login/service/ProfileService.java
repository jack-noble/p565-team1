package com.infinitycare.health.login.service;

import com.infinitycare.health.database.DoctorRepository;
import com.infinitycare.health.database.IpPlanRepository;
import com.infinitycare.health.database.IpRepository;
import com.infinitycare.health.database.PatientRepository;
import com.infinitycare.health.login.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class ProfileService extends ServiceUtility {

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
        String username = getUsername(request);

        Map<String, Object> result = new HashMap<>();

        if (userType.equals(PATIENT)) {
            Optional<PatientDetails> userQueriedFromDB = patientRepository.findById(Integer.toString(username.hashCode()));
            if (userQueriedFromDB.isPresent()) {
                result.put("username", username);
                result.put("email", userQueriedFromDB.get().mEmail);
                result.put("firstname", userQueriedFromDB.get().mFirstName);
                result.put("lastname", userQueriedFromDB.get().mLastName);
                result.put("address", userQueriedFromDB.get().mAddress);
                result.put("phonenumber", userQueriedFromDB.get().mPhoneNumber);
                result.put("dob", userQueriedFromDB.get().mDOB);
                result.put("insurancecompany", userQueriedFromDB.get().mInsuranceCompany);
                result.put("insuranceplan", userQueriedFromDB.get().mInsurancePlan);
                result.put("emergencycontactname", userQueriedFromDB.get().mEmergencyContactName);
                result.put("emergencycontactnumber", userQueriedFromDB.get().mEmergencyContactNumber);
                result.put("medicalhistory", userQueriedFromDB.get().mMedicalHistory);
                result.put("bloodType", userQueriedFromDB.get().getBloodType());
                result.put("allergies", userQueriedFromDB.get().getAllergies());
                result.put("currentMedications", userQueriedFromDB.get().getCurrentMedications());
                result.put("vaccinations", userQueriedFromDB.get().getVaccinations());

                IPDetails insuranceProviderDetails = getDetailsOfInsuranceProviderWhoCreatedThePlan(ipRepository, userQueriedFromDB.get().getInsurancePlan());
                if(insuranceProviderDetails != null) {
                    result.put("insuranceprovider", insuranceProviderDetails.mFirstName + " " + insuranceProviderDetails.mLastName);
                }

                Optional<IpPlanDetails> insuranceProvider = ipPlanRepository.findById(Integer.toString(userQueriedFromDB.get().getInsurancePlan().hashCode()));
                if(insuranceProvider.isPresent()) {
                    result.put("premium", insuranceProvider.get().getPremium());
                    result.put("deductible", insuranceProvider.get().getDeductible());
                    result.put("copayment", insuranceProvider.get().getCoPayment());
                    result.put("outofpocketlimit", insuranceProvider.get().getAnnualOutOfPocketLimit());
                }

                Optional<IPDetails> ipFromDB = ipRepository.findById(Integer.toString(userQueriedFromDB.get().mInsuranceProvider.hashCode()));
                ipFromDB.ifPresent(ipDetails -> result.put("insuranceprovidername", (ipDetails.mFirstName + " " + ipDetails.mLastName)));
            }
        }

        if (userType.equals(DOCTOR)) {
            Optional<DoctorDetails> userQueriedFromDB = doctorRepository.findById(Integer.toString(username.hashCode()));
            if (userQueriedFromDB.isPresent()) {
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

        if (userType.equals(INSURANCE_PROVIDER)) {
            Optional<IPDetails> userQueriedFromDB = ipRepository.findById(Integer.toString(username.hashCode()));
            if (userQueriedFromDB.isPresent()) {
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

    public ResponseEntity<?> updateDoctorProfile(HttpServletRequest request) {
        String username = getUsername(request);

        Map<String, Object> result = new HashMap<>();
        boolean isProfileUpdated = true;

        Map<String, String> postBody = getPostBodyInAMap(request);
        Optional<DoctorDetails> userQueriedFromDB = doctorRepository.findById(Integer.toString(username.hashCode()));

        if (!userQueriedFromDB.isPresent()) {
            result.put("userExists", false);
            return ResponseEntity.ok(result);
        }

        DoctorDetails doctorDetails = userQueriedFromDB.get();

        if (!StringUtils.isEmpty(postBody.get("address")))
            doctorDetails.setAddress(postBody.get("address"));
        if (!StringUtils.isEmpty(postBody.get("hospital")))
            doctorDetails.setHospital(postBody.get("hospital"));
        if (!StringUtils.isEmpty(postBody.get("specialization")))
            doctorDetails.setSpecialization(postBody.get("specialization"));
        if (!StringUtils.isEmpty(postBody.get("biosummary")))
            doctorDetails.setPersonalBio(postBody.get("biosummary"));
        if (!StringUtils.isEmpty(postBody.get("emailaddress")))
            doctorDetails.setEmail(postBody.get("emailaddress"));
        if (!StringUtils.isEmpty(postBody.get("education")))
            doctorDetails.setEducation(postBody.get("education"));

        doctorRepository.save(doctorDetails);
        result.put("isProfileUpdated", isProfileUpdated);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> updatePatientProfile(HttpServletRequest request) {
        String username = getUsername(request);

        Map<String, Object> result = new HashMap<>();
        boolean isProfileUpdated = false;

        Map<String, String> postBody = getPostBodyInAMap(request);

        Optional<PatientDetails> userQueriedFromDB = patientRepository.findById(Integer.toString(username.hashCode()));

        if (!userQueriedFromDB.isPresent()) {
            result.put("userExists", false);
            return ResponseEntity.ok(result);
        }
        PatientDetails patientDetails = userQueriedFromDB.get();

        if (!StringUtils.isEmpty(postBody.get("emailaddress")))
            patientDetails.setEmail(postBody.get("emailaddress"));
        if (!StringUtils.isEmpty(postBody.get("address")))
            patientDetails.setAddress(postBody.get("address"));
        if (!StringUtils.isEmpty(postBody.get("phonenumber")))
            patientDetails.setPhoneNumber(postBody.get("phonenumber"));

        if (!StringUtils.isEmpty(postBody.get("emergencycontactname")))
            patientDetails.setmEmergencyContactName(postBody.get("emergencycontactname"));
        if (!StringUtils.isEmpty(postBody.get("emergencycontactnumber")))
            patientDetails.setmEmergencyContactNumber(postBody.get("emergencycontactnumber"));
        if (!StringUtils.isEmpty(postBody.get("medicalhistory")))
            patientDetails.setmMedicalHistory(postBody.get("medicalhistory"));

        if (!StringUtils.isEmpty(postBody.get("bloodType")))
            patientDetails.setBloodType(postBody.get("bloodType"));
        if (!StringUtils.isEmpty(postBody.get("allergies")))
            patientDetails.setAllergies(postBody.get("allergies"));
        if (!StringUtils.isEmpty(postBody.get("currentMedications")))
            patientDetails.setCurrentMedications(postBody.get("currentMedications"));
        if (!StringUtils.isEmpty(postBody.get("vaccinations")))
            patientDetails.setVaccinations(postBody.get("vaccinations"));

        patientRepository.save(patientDetails);
        result.put("isProfileUpdated", isProfileUpdated);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> updateIpProfile(HttpServletRequest request) {
        String username = getUsername(request);

        Map<String, Object> result = new HashMap<>();
        boolean isProfileUpdated = false;

        Map<String, String> postBody = getPostBodyInAMap(request);

        Optional<IPDetails> userQueriedFromDB = ipRepository.findById(Integer.toString(username.hashCode()));

        if (!userQueriedFromDB.isPresent()) {
            result.put("userExists", false);
            return ResponseEntity.ok(result);
        }

        IPDetails ipDetails = userQueriedFromDB.get();
        if (!StringUtils.isEmpty(postBody.get("emailaddress")))
            ipDetails.setEmail(postBody.get("emailaddress"));
        if (!StringUtils.isEmpty(postBody.get("address")))
            ipDetails.setAddress(postBody.get("address"));
        if (!StringUtils.isEmpty(postBody.get("phonenumber")))
            ipDetails.setPhoneNumber(postBody.get("phonenumber"));

        ipRepository.save(ipDetails);
        result.put("isProfileUpdated", isProfileUpdated);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> getDoctorFromPatient(String doctorusername) {
        Map<String, Object> result = new HashMap<>();

        Optional<DoctorDetails> doctorQueriedFromDB = doctorRepository.findById(Integer.toString(doctorusername.hashCode()));
        if (doctorQueriedFromDB.isPresent()) {
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
        if (patientQueriedFromDB.isPresent()) {
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
        if (ipFromDB.isPresent()) {
            result.put("name", ipFromDB.get().mFirstName + " " + ipFromDB.get().mLastName);
            result.put("company", ipFromDB.get().mCompany);
            result.put("address", ipFromDB.get().mAddress);
            result.put("phonenumber", ipFromDB.get().mPhoneNumber);
        }

        return ResponseEntity.ok(result);
    }

}
