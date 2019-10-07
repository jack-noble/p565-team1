package com.infinitycare.health;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import com.infinitycare.health.login.model.*;

@SpringBootApplication
public class HealthApplication {

    @Autowired
    UserRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(HealthApplication.class, args);
    }

}
