package com.infinitycare.health.database;

// database package is for testing

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<Users, String> {
}
