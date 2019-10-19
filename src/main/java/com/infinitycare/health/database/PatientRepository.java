package com.infinitycare.health.database;

import com.infinitycare.health.login.model.PatientDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends MongoRepository<PatientDetails, String>{
}
