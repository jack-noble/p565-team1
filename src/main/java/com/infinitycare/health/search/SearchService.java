package com.infinitycare.health.search;

import com.infinitycare.health.database.DoctorRepository;
import com.infinitycare.health.database.IpRepository;
import com.infinitycare.health.database.PatientRepository;
import com.infinitycare.health.login.model.ServiceUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class SearchService extends ServiceUtility {

    public static final String IS_LOCATION_ENABLED = "isLocationEnabled";
    public static final String IS_SPECIALIZATION_ENABLED = "isSpecializationEnabled";
    @Autowired
    MongoOperations mongoOperations;

    @Autowired
    public PatientRepository patientRepository;

    @Autowired
    public DoctorRepository doctorRepository;

    @Autowired
    public IpRepository ipRepository;

    public ResponseEntity<?> searchForUsers(HttpServletRequest request, String userType, String query) {
        Set<Object> result = new HashSet<>();

        Map<String, String> postBody = getPostBodyInAMap(request);
        boolean isLocationSearchEnabled = Boolean.getBoolean(postBody.get(IS_LOCATION_ENABLED));
        boolean isSpecializationSearchEnabled = Boolean.getBoolean(postBody.get(IS_SPECIALIZATION_ENABLED));

        switch (userType) {
            case "patient": {
                result.add(patientRepository.findByPatientsWithSimilarName(query));
                break;
            }

            case "doctor": {
                if(isSpecializationSearchEnabled)
                    result.add(doctorRepository.findDoctorsWithSimilarSpecializations(query));
                else
                    result.add(doctorRepository.findDoctorsWithSimilarName(query));
                break;
            }

            case "insurance": {
                result.add(ipRepository.findInsuranceProvidersWithSimilarName(query));
                break;
            }

            default :
                throw new RuntimeException("Wrong user type");
        }

        return ResponseEntity.ok(result);
    }

}
