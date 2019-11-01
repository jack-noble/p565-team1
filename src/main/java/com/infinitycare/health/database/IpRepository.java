package com.infinitycare.health.database;

import com.infinitycare.health.login.model.IPDetails;
import com.infinitycare.health.login.model.IpPlanDetails;
import com.infinitycare.health.login.model.PatientDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IpRepository extends MongoRepository<IPDetails, String> {

    @Query("{mUserName: { $regex: ?0 }})")
    List<IpPlanDetails> findInsuranceProvidersWithSimilarName(String insuranceProvider);
}
