package com.infinitycare.health.database;

import com.infinitycare.health.login.model.IPDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IpRepository extends MongoRepository<IPDetails, String>{
}
