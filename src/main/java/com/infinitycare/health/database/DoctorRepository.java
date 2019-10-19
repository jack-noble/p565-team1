package com.infinitycare.health.database;

import com.infinitycare.health.login.model.DoctorDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends MongoRepository<DoctorDetails, String>{
}
