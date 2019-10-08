package com.infinitycare.health.login.controller;

import com.infinitycare.health.database.UserRepository;
import com.infinitycare.health.login.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Autowired
    UserRepository repository;

    @RequestMapping("/")
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("login.html");
        return mv;
    }

    @RequestMapping(value = "/LoggedIn")
    public ModelAndView test(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();

        //Check the username and password against the data in Database
        //Should use an encrypted password while matching against the rows in a DB. Would be ideal if we are able to send an encrypted password
        mv.addObject("username", request.getParameter("username"));
        //SendEmail.send();
        mv.setViewName("LoggedIn.html");
        return mv;
    }

    @RequestMapping(value = "/signin")
    @ResponseBody
    public String login(HttpServletRequest request, @PathVariable String userType) {

        UserDetails userDetails = new UserDetails(request.getParameter("username"), request.getParameter("username"), userType);
        checkIfCredentialsAreAccurate(userDetails);

        System.out.println("Signing in");
        //Check the username and password against the data in Database
        //Should use an encrypted password while matching against the rows in a DB. Would be ideal if we are able to send an ecrypted password
        return "";
    }

    private boolean checkIfCredentialsAreAccurate(UserDetails userDetails) {
        String enteredUsername = userDetails.mUserName;
        String enteredPassword = userDetails.mPassword;

        // searches for the 1 unique user
        int searchForUser = repository.find(
                {"mUserName": enteredUsername, "mPassword": enteredPassword}
        ).toArray().length;
        // int searchForUser = repository.find(
        // {"mUserName": enteredUsername, "mPassword": enteredPassword}
        //.toArray()[0].length; // may need to index into array to get proper count

        if(searchForUser == 1)
            return true; // there exists a unique user w/ matching credentials
        else
            return false; // user not found in database
    }

}
