package com.infinitycare.health.database;

import com.infinitycare.health.login.model.UserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserDetails, String> {
}
