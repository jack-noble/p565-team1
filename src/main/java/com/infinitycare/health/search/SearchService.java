package com.infinitycare.health.search;

import com.infinitycare.health.database.DoctorRepository;
import com.infinitycare.health.database.IpRepository;
import com.infinitycare.health.database.PatientRepository;
import com.infinitycare.health.login.model.DoctorDetails;
import com.infinitycare.health.login.model.IPDetails;
import com.infinitycare.health.login.model.PatientDetails;
import com.infinitycare.health.login.model.ServiceUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SearchService extends ServiceUtility {

    @Autowired
    MongoOperations mongoOperations;

    @Autowired
    public PatientRepository patientRepository;

    @Autowired
    public DoctorRepository doctorRepository;

    @Autowired
    public IpRepository ipRepository;

    public ResponseEntity<?> searchForUsers(String userType, String query) {
        query = query.trim();
        //boolean isLocationSearchEnabled = Boolean.getBoolean(postBody.get(IS_LOCATION_ENABLED));
        //boolean isSpecializationSearchEnabled = Boolean.getBoolean(postBody.get(IS_SPECIALIZATION_ENABLED));

        switch (userType) {
            case "patient": {
                Set<PatientDetails> details = new HashSet<>();
                details.addAll(patientRepository.findByPatientsWithSimilarFirstName(query));
                details.addAll(patientRepository.findByPatientsWithSimilarLastName(query));
                return ResponseEntity.ok(details);
            }

            case "doctor": {
                Set<DoctorDetails> details = new HashSet<>();
                String[] arr = query.split(" ");
                for(String word : arr) {
                    if(word.equalsIgnoreCase("in") || word.equalsIgnoreCase(("specialized")) || word.equalsIgnoreCase(("speciality"))) {
                        details.addAll(doctorRepository.findDoctorsWithSimilarSpecializations(arr[arr.length - 1]));
                        details.addAll(doctorRepository.findDoctorsWithSimilarLocations(arr[arr.length - 1]));
                        return ResponseEntity.ok(details);
                    }
                }

                details.addAll(doctorRepository.findDoctorsWithSimilarFirstName(query));
                details.addAll(doctorRepository.findDoctorsWithSimilarLastName(query));
                return ResponseEntity.ok(details);
            }

            case "insurance": {
                Set<IPDetails> details = new HashSet<>();
                details.addAll(ipRepository.findInsuranceProvidersWithSimilarFirstName(query));
                details.addAll(ipRepository.findInsuranceProvidersWithSimilarLastName(query));
                return ResponseEntity.ok(details);
            }

            default :
                throw new RuntimeException("Wrong user type");
        }
    }

    public ResponseEntity<?> getLocations(String userType, String query) {
        query = query.trim();

        switch (userType) {
            case "patient": {
                Set<String> details = new HashSet<>();
                patientRepository.findByPatientsWithSimilarFirstName(query).forEach(patient -> details.add(patient.getAddress()));
                patientRepository.findByPatientsWithSimilarLastName(query).forEach(patient -> details.add(patient.getAddress()));
                return ResponseEntity.ok(details.stream().filter(address -> !address.equals("")).collect(Collectors.toSet()));
            }

            case "doctor": {
                Set<String> details = new HashSet<>();
                String[] arr = query.split(" ");
                for(String word : arr) {
                    if(word.equalsIgnoreCase("in") || word.equalsIgnoreCase(("specialized")) || word.equalsIgnoreCase(("speciality"))) {
                        doctorRepository.findDoctorsWithSimilarSpecializations(arr[arr.length - 1]).forEach(doctor -> details.add(doctor.getAddress()));
                        doctorRepository.findDoctorsWithSimilarLocations(arr[arr.length - 1]).forEach(doctor -> details.add(doctor.getAddress()));
                        return ResponseEntity.ok(details);
                    }
                }

                doctorRepository.findDoctorsWithSimilarFirstName(query).forEach(doctor -> details.add(doctor.getAddress()));
                doctorRepository.findDoctorsWithSimilarLastName(query).forEach(doctor -> details.add(doctor.getAddress()));
                return ResponseEntity.ok(details.stream().filter(address -> !address.equals("")).collect(Collectors.toSet()));
            }

            case "insurance": {
                Set<String> details = new HashSet<>();
                ipRepository.findInsuranceProvidersWithSimilarFirstName(query).forEach(doctor -> details.add(doctor.getAddress()));
                ipRepository.findInsuranceProvidersWithSimilarLastName(query).forEach(doctor -> details.add(doctor.getAddress()));
                return ResponseEntity.ok(details.stream().filter(address -> !address.equals("")).collect(Collectors.toSet()));
            }

            default :
                throw new RuntimeException("Wrong user type");
        }
    }

}
