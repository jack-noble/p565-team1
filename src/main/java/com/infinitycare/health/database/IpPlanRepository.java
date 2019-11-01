package com.infinitycare.health.database;

import com.infinitycare.health.login.model.IpPlanDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpPlanRepository extends MongoRepository<IpPlanDetails, String> {
}
