package com.infinitycare.health;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.infinitycare.health.login.model.*;

@SpringBootApplication
public class HealthApplication {

    UserRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(HealthApplication.class, args);
    }

}
