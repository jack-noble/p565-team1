package com.infinitycare.health.login.service;

import com.infinitycare.health.database.DoctorRepository;
import com.infinitycare.health.database.IpPlanRepository;
import com.infinitycare.health.database.IpRepository;
import com.infinitycare.health.database.PatientRepository;
import com.infinitycare.health.login.model.*;
import com.mongodb.BasicDBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class DashboardService extends ServiceUtility {

    @Autowired
    public PatientRepository patientRepository;

    @Autowired
    public IpRepository ipRepository;

    @Autowired
    public DoctorRepository doctorRepository;

    @Autowired
    public IpPlanRepository ipPlanRepository;

    public DashboardService(PatientRepository patientRepository, IpRepository ipRepository, DoctorRepository doctorRepository, IpPlanRepository ipPlanRepository) {
        this.patientRepository = patientRepository;
        this.ipRepository = ipRepository;
        this.doctorRepository = doctorRepository;
        this.ipPlanRepository = ipPlanRepository;
    }

    public ResponseEntity<?> getIplans(HttpServletRequest request) {
        String username = getUsername(request);

        ArrayList iplans = new ArrayList();
        List<Optional<IpPlanDetails>> iPlans = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();

        Optional<IPDetails> userQueriedFromDB = ipRepository.findById(Integer.toString(username.hashCode()));
        if(userQueriedFromDB.isPresent()) { iplans = userQueriedFromDB.get().mIplans; }

        for (Object iplan : iplans) {
            Optional<IpPlanDetails> planFromDB = ipPlanRepository.findById(Integer.toString(iplan.hashCode()));
            if (planFromDB.isPresent()) { iPlans.add(planFromDB); }
        }

        result.put("IPlans", iPlans);
        result.put("Patients", getPatientsList(request));
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> editIplans(HttpServletRequest request, String action) {
        String username = getUsername(request);

        Map<String, Object> result = new HashMap<>();
        ArrayList iplans = new ArrayList();

        boolean isIplansUpdated = false;

        IpPlanDetails ipPlanDetails = new IpPlanDetails(getPostBodyInAMap(request).get("name"), getPostBodyInAMap(request).get("provider"),
                                                        getPostBodyInAMap(request).get("price"), getPostBodyInAMap(request).get("details"));

        Optional<IPDetails> userQueriedFromDB = ipRepository.findById(Integer.toString(username.hashCode()));
        if(userQueriedFromDB.isPresent()) {
            IPDetails ipDetails = userQueriedFromDB.get();
            iplans = userQueriedFromDB.get().mIplans;
            if(action.equals("add")) {
                ipPlanRepository.save(ipPlanDetails);
                iplans.add(ipPlanDetails.mName);
                isIplansUpdated = true;
            }
            if(action.equals("delete")) {
                iplans.remove(ipPlanDetails.mName);
                isIplansUpdated = true;
            }
            ipDetails.setmIplans(iplans);
            ipRepository.save(ipDetails);
        }

        result.put("isIplansUpdated", isIplansUpdated);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> getPatientsListForIp(HttpServletRequest request) {
        Map<String, Object> result = new HashMap();

        result.put("patients", getPatientsList(request));
        return ResponseEntity.ok(result);
    }

    private List<IPDetails> getPatientsList(HttpServletRequest request) {
        String username = getUsername(request);

        Map<String, Object> result = new HashMap<>();
        ArrayList finalpatients = new ArrayList();

        Optional<IPDetails> ipFromDB = ipRepository.findById(Integer.toString(username.hashCode()));

        if(ipFromDB.isPresent()) {
            ArrayList patients = ipFromDB.get().mPatients;

            for (Object o : patients) {
                Map<String, Object> patient = new HashMap<>();
                Optional<PatientDetails> patientFromDB = patientRepository.findById(Integer.toString(o.hashCode()));
                if(patientFromDB.isPresent()) {
                    patient.put("username", o);
                    patient.put("name", patientFromDB.get().mFirstName + " " + patientFromDB.get().mLastName);
                }
                finalpatients.add(patient);
            }
        }
        return finalpatients;
    }

    public ResponseEntity<?> addReviewsForDoctor(HttpServletRequest request) {
        String doctorusername = getUsername(request);
        String review = getPostBodyInAMap(request).get("review");
        int rating = Integer.parseInt(getPostBodyInAMap(request).get("rating"));

        Map<String, Object> result = new HashMap<>();
        boolean isReviewAdded = false;

        Optional<DoctorDetails> doctorFromDB = doctorRepository.findById(Integer.toString(doctorusername.hashCode()));
        if(doctorFromDB.isPresent()) {
            DoctorDetails doctorDetails = doctorFromDB.get();
            ArrayList reviews = doctorFromDB.get().mReviews;
            BasicDBObject newReview = new BasicDBObject();
            newReview.put("rating", rating);
            newReview.put("review", review);
            reviews.add(newReview);
            doctorDetails.setReviews(reviews);

            // updating the cumulative rating
            if(doctorFromDB.get().mTotalRating == 0) { doctorDetails.setTotalRating(rating); }
            else { doctorDetails.setTotalRating((doctorFromDB.get().mTotalRating * (reviews.size() - 1) + rating)/reviews.size()); }

            doctorRepository.save(doctorDetails);
            isReviewAdded = true;
        }

        result.put("isReviewAdded", isReviewAdded);
        return ResponseEntity.ok(result);
    }

}
