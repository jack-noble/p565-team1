package com.infinitycare.health.login.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<UserRepo, String> {
}
