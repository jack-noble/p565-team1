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
        List<IpPlanDetails> iPlans = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();

        Optional<IPDetails> userQueriedFromDB = ipRepository.findById(Integer.toString(username.hashCode()));
        if(userQueriedFromDB.isPresent()) {
            iplans = userQueriedFromDB.get().mIplans;
        }

        for (Object iplan : iplans) {
            Optional<IpPlanDetails> planFromDB = ipPlanRepository.findById(Integer.toString(iplan.hashCode()));
            if (planFromDB.isPresent()) {
                iPlans.add(planFromDB.get());
            }
        }

        result.put("IPlans", iPlans);
        result.put("Patients", getPatientsList(request));
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> editIplans(HttpServletRequest request, String action) {
        String username = getUsername(request);

        Map<String, Object> result = new HashMap<>();
        ArrayList iplans = new ArrayList();

        Map<String, String> postBody = getPostBodyInAMap(request);

        Optional<IPDetails> userQueriedFromDB = ipRepository.findById(Integer.toString(username.hashCode()));
        if(userQueriedFromDB.isPresent()) {
            IpPlanDetails ipPlanDetails = new IpPlanDetails(postBody.get("name"), userQueriedFromDB.get().getCompany());
            ipPlanDetails.setPremium(postBody.get("premium"));
            ipPlanDetails.setDeductible(postBody.get("deductible"));
            ipPlanDetails.setCoPayment(postBody.get("copayment"));
            ipPlanDetails.setAnnualOutOfPocketLimit(postBody.get("outofpocketlimit"));
            ipPlanDetails.setLevel(postBody.get("level"));

            IPDetails ipDetails = userQueriedFromDB.get();
            iplans = userQueriedFromDB.get().mIplans;
            if(action.equals("add")) {
                ipPlanRepository.save(ipPlanDetails);
                iplans.add(ipPlanDetails.mName);
                result.put("planName", "Added " + ipPlanDetails.mName);
            }
            if(action.equals("delete")) {
                iplans.remove(ipPlanDetails.mName);
                result.put("planName", "Deleted " + ipPlanDetails.mName);
            }
            ipDetails.setmIplans(iplans);
            ipRepository.save(ipDetails);
        }

        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> getPatientsListForIp(HttpServletRequest request) {
        Map<String, Object> result = new HashMap();

        result.put("patients", getPatientsList(request));
        return ResponseEntity.ok(result);
    }

    private List<IPDetails> getPatientsList(HttpServletRequest request) {
        String username = getUsername(request);

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
                    patient.put("currentplan", patientFromDB.get().mInsurancePlan);
                }
                finalpatients.add(patient);
            }
        }
        return finalpatients;
    }

    public ResponseEntity<?> addReviewsForDoctor(HttpServletRequest request, String doctorusername) {
        Map<String, String> postBody = getPostBodyInAMap(request);
        String review = postBody.get("review");
        int rating = Integer.parseInt(postBody.get("rating"));

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
