package com.infinitycare.health.recommendations;

import com.infinitycare.health.database.IpPlanRepository;
import com.infinitycare.health.database.IpRepository;
import com.infinitycare.health.database.PatientRepository;
import com.infinitycare.health.login.model.IPDetails;
import com.infinitycare.health.login.model.IpPlanDetails;
import com.infinitycare.health.login.model.PatientDetails;
import com.infinitycare.health.login.model.ServiceUtility;
import com.infinitycare.health.login.model.comparator.SortInsurancePlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class RecommendationsService extends ServiceUtility {

    @Autowired
    public PatientRepository patientRepository;

    @Autowired
    public IpPlanRepository ipPlanRepository;

    @Autowired
    public IpRepository ipRepository;

    public RecommendationsService(PatientRepository patientRepository, IpPlanRepository ipPlanRepository, IpRepository ipRepository) {
        this.patientRepository = patientRepository;
        this.ipPlanRepository = ipPlanRepository;
        this.ipRepository = ipRepository;
    }

    public ResponseEntity<?> saveRecommendationWeights(HttpServletRequest request) {
        String username = getUsername(request);
        Map<String, String> postBody = getPostBodyInAMap(request);

        Optional<PatientDetails> patientQueriedFromDB = patientRepository.findById(Integer.toString(username.hashCode()));
        if (patientQueriedFromDB.isPresent()) {
            patientQueriedFromDB.get().setIncomeValue(Integer.valueOf(postBody.get("incomeValue")));
            patientQueriedFromDB.get().setRecommendationWeight(Integer.valueOf(postBody.get("finalWeight")));

            patientRepository.save(patientQueriedFromDB.get());
        }

        return ResponseEntity.ok(new HashMap<>());
    }

    public ResponseEntity<?> getRecommendations(HttpServletRequest request) {
        String username = getUsername(request);
        Set<IpPlanDetails> result = new TreeSet<>(new SortInsurancePlan());

        Optional<PatientDetails> patientQueriedFromDB = patientRepository.findById(Integer.toString(username.hashCode()));
        if (patientQueriedFromDB.isPresent()) {
            int incomeValue = patientQueriedFromDB.get().getIncomeValue();
            int recommendationsWeight = patientQueriedFromDB.get().getRecommendationWeight();
            List<IpPlanDetails> plans = ipPlanRepository.findAll();

            if(incomeValue < 2) {
                //Cannot recommend gold or platinum plans for people with low income.
                if(recommendationsWeight > 2) {
                    // Recommending silver
                    plans.removeIf(plan -> plan.getWeightOfTheLevel() != 1);
                } else {
                    // Recommending bronze
                    plans.removeIf(plan -> plan.getWeightOfTheLevel() != 0);
                }
            } else if (incomeValue < 3) {
                // Won't recommend bronze.
                if(recommendationsWeight == 4) {
                    // Recommending Platinum
                    plans.removeIf(plan -> plan.getWeightOfTheLevel() != 3);
                } else if(recommendationsWeight >= 2) {
                    // Recommending Gold
                    plans.removeIf(plan -> plan.getWeightOfTheLevel() != 2);
                } else {
                    // Recommending bronze
                    plans.removeIf(plan -> plan.getWeightOfTheLevel() != 1);
                }
            } else {
                // Rich people. Lets not recommend gold or silver.
                if(recommendationsWeight > 2) {
                    // Recommending Platinum
                    plans.removeIf(plan -> plan.getWeightOfTheLevel() != 3);
                } else {
                    // Recommending Gold
                    plans.removeIf(plan -> plan.getWeightOfTheLevel() != 2);
                }
            }

            for(IpPlanDetails plan : plans) {
                IPDetails insuranceProvider = getDetailsOfInsuranceProviderWhoCreatedThePlan(ipRepository, plan.getName());
                if(insuranceProvider == null) {
                    continue;
                }
                plan.setFirstNameOfCreator(insuranceProvider.mFirstName);
                plan.setLastNameOfCreator(insuranceProvider.mLastName);
                result.add(plan);
            }
        }

        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> getPlanDetails(String planName) {
        IpPlanDetails result = null;

        Optional<IpPlanDetails> planQueriedFromDB = ipPlanRepository.findById(Integer.toString(planName.hashCode()));
        if (planQueriedFromDB.isPresent()) {
            result = planQueriedFromDB.get();
        }

        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> updateInsurancePlanOfThePatient(HttpServletRequest request) {
        String username = getUsername(request);
        Map<String, String> postBody = getPostBodyInAMap(request);
        String planName = postBody.get("planName");


        Optional<PatientDetails> patientQueriedFromDB = patientRepository.findById(Integer.toString(username.hashCode()));
        if (patientQueriedFromDB.isPresent()) {
            patientQueriedFromDB.get().setInsurancePlan(planName);

            IPDetails insuranceProvider = getInsuranceProviderDetails(planName);
            patientQueriedFromDB.get().setInsuranceProvider(insuranceProvider.getUserName());
            patientQueriedFromDB.get().setInsuranceCompany(insuranceProvider.getCompany());

            patientRepository.save(patientQueriedFromDB.get());
            return ResponseEntity.ok("updated");
        }

        return ResponseEntity.ok("nothing to update");
    }

    private IPDetails getInsuranceProviderDetails(String insurancePlan) {
        List<IPDetails> insuranceProviders = ipRepository.findAll();

        IPDetails result = null;
        for (IPDetails insuranceProvider : insuranceProviders) {
            if(insuranceProvider.getIpPlans().contains(insurancePlan)) {
                result = insuranceProvider;
                break;
            }
        }

        return result;
    }

}
