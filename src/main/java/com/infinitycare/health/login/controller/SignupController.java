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
import java.util.Optional;

@Controller
public class SignupController {

    @Autowired
    UserRepository repository;

    @RequestMapping(value = "/signup/{userType}")
    @ResponseBody
    public String signup(HttpServletRequest request, @PathVariable String userType) {

        UserDetails userDetails = new UserDetails(request.getParameter("username"), request.getParameter("password"), userType);
        if(doesUserAlreadyExist(userDetails)){
            System.out.println("Error: this account already exists, log in or reset your password");
            // TODO - display to front end, if method returns var account_already_exists display error
            return "account_already_exists";
        } else{
            repository.save(userDetails);
            System.out.println("Signing up");
            SendEmailSMTP.sendFromGMail(new String[]{request.getParameter("username")}, "Please enter the OTP in the signup screen", SendEmailSMTP.generateRandomNumber(1000, 9999));

            return "";
        }
    }

    public boolean doesUserAlreadyExist(UserDetails userDetails){

        String enteredUsername = userDetails.getUserName();
        Optional<UserDetails> userQueriedFromDB = repository.findById(Integer.toString(enteredUsername.hashCode()));

        // returns if user is present
        return userQueriedFromDB.isPresent();
    }

}
