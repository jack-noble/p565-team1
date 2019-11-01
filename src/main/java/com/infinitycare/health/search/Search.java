package com.infinitycare.health.search;

import com.infinitycare.health.database.DoctorRepository;
import com.infinitycare.health.database.IpRepository;
import com.infinitycare.health.database.PatientRepository;
import com.infinitycare.health.login.model.PatientDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
public class Search {

    @Autowired
    MongoOperations mongoOperations;

    @Autowired
    public PatientRepository patientRepository;

    @Autowired
    public DoctorRepository doctorRepository;

    @Autowired
    public IpRepository ipRepository;

    public ResponseEntity<?> searchForUsers(HttpServletRequest request, String userType, String userName) {
        Map<String, Object> result = new HashMap<>();

        switch (userType) {

            case "patient": {
                patientRepository.findAll(new Example<PatientDetails>() {
                    @Override
                    public PatientDetails getProbe() {
                        return null;
                    }

                    @Override
                    public ExampleMatcher getMatcher() {
                        return null;
                    }
                });
                break;
            }

            case "doctor": {
                break;
            }

            case "insurance": {

                break;
            }

            default :
                throw new RuntimeException("Wrong user type");
        }

        return ResponseEntity.ok(result);
    }
}
