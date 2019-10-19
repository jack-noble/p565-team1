package com.infinitycare.health.database;

import com.infinitycare.health.login.model.IPDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpRepository extends MongoRepository<IPDetails, String> {
}
