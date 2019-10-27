package com.infinitycare.health.database;

import com.infinitycare.health.login.model.AppointmentsDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentsRepository extends MongoRepository<AppointmentsDetails, String>{

    @Query("{ 'mPatientUsername' : ?0 }")
    public List<AppointmentsDetails> findAllPatientAppointments(String mPatientusername);

    @Query("{ 'mDoctorUsername' : ?0 }")
    public List<AppointmentsDetails> findAllDoctorAppointments(String mDoctorUsername);
}
