package com.infinitycare.health.database;

import com.infinitycare.health.login.model.DoctorDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends MongoRepository<DoctorDetails, String> {

    @Query("{mUserName: { $regex: ?0 }})")
    List<DoctorDetails> findDoctorsWithSimilarUserName(String doctorName);

    @Query("{mFirstName: { $regex: ?0 }})")
    List<DoctorDetails> findDoctorsWithSimilarFirstName(String patientName);

    @Query("{mLastName: { $regex: ?0 }})")
    List<DoctorDetails> findDoctorsWithSimilarLastName(String patientName);

    @Query("{mSpecialization: { $regex: ?0 }})")
    List<DoctorDetails> findDoctorsWithSimilarSpecializations(String specialization);

    @Query("{mAddress: { $regex: ?0 }})")
    List<DoctorDetails> findDoctorsWithSimilarLocations(String specialization);
}
