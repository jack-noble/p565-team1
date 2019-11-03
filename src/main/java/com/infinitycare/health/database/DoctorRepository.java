package com.infinitycare.health.database;

import com.infinitycare.health.login.model.DoctorDetails;
import com.infinitycare.health.login.model.PatientDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends MongoRepository<DoctorDetails, String> {

    @Query("{mUserName: { $regex: ?0 }})")
    List<DoctorDetails> findDoctorsWithSimilarName(String doctorName);

    @Query("{mSpecialization: { $regex: ?0 }})")
    List<DoctorDetails> findDoctorsWithSimilarSpecializations(String specialization);
}
