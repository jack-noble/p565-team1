package com.infinitycare.health.login.controller;

import com.infinitycare.health.database.UserRepository;
import com.infinitycare.health.login.SendEmailSMTP;
import com.infinitycare.health.login.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SignupController {

    @Autowired
    UserRepository repository;

    @RequestMapping(value = "/signup/{userType}/")
    @ResponseBody
    public String signup(HttpServletRequest request, @PathVariable String userType) {

        UserDetails userDetails = new UserDetails(request.getParameter("username"), request.getParameter("username"), userType);
        repository.save(userDetails);

        System.out.println("Signing up");

        String body = "<a href=\"https://www.w3schools.com\">Please verify if you've created an account with InfinityCare</a>";
        SendEmailSMTP.sendFromGMail(new String[]{request.getParameter("username")}, "Welcome to InfinityCare", body);

        return "";
    }
}
