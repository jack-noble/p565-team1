package com.infinitycare.health.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// database package is for testing

@SpringBootApplication
public class TestingDB implements CommandLineRunner {

    int idIter=0;

    @Autowired
    UserRepository repository;

    public static void main(String[] args){
        SpringApplication.run(TestingDB.class,args);
    }
    @Override
    public void run(String... args) throws Exception {
        deleteAll();
        addSampleData();
        listAll();
    }

    public void deleteAll() {
        System.out.println("Deleting all records..");
        repository.deleteAll();
    }

    public void addSampleData() {
        System.out.println("Adding sample data");
        repository.save(new Users(++idIter, "y32o@gmail.com", "3242", "Doctor"));
        repository.save(new Users(++idIter, "dw45cool@gmail.com", "2322222d", "Patient"));
        repository.save(new Users(++idIter, "ne322hg@gmail.com", "wwe3232", "Insurance Provider"));
        repository.save(new Users(++idIter, "kathyku@gmail.com", "44444d", "Patient"));
    }

    public void listAll() {
        System.out.println("Listing sample data");
        repository.findAll().forEach(u -> System.out.println(u.getEmail()));
    }
}



