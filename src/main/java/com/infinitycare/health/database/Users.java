package com.infinitycare.health.database;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// database package is for testing



@Document(collection = "users")
public class Users {

    @Id
    private int id;
    private String email;
    private String password;
    private String typeOfUser;

    public Users(int id, String email, String password, String typeOfUser) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.typeOfUser = typeOfUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTypeOfUser() {
        return typeOfUser;
    }

    public void setTypeOfUser(String typeOfUser) {
        this.typeOfUser = typeOfUser;
    }
}
