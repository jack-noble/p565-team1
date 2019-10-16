package com.infinitycare.health.database;

import com.infinitycare.health.login.model.PatientDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PatientRepository extends MongoRepository<PatientDetails, String>{
}
