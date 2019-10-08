package com.infinitycare.health.login.controller;

import com.infinitycare.health.database.UserRepository;
import com.infinitycare.health.login.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import static org.springframework.data.mongodb.core.query.Criteria.where;

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
        repository.save(new UserDetails(request.getParameter("username"), request.getParameter("password"), ""));
        //SendEmail.send();
        mv.setViewName("LoggedIn.html");
        return mv;
    }

    @RequestMapping(value = "/signin/{userType}")
    @ResponseBody
    public String login(HttpServletRequest request, @PathVariable String userType) {

        UserDetails userDetails = new UserDetails(request.getParameter("username"), request.getParameter("password"), userType);
        repository.save(userDetails);
        checkIfCredentialsAreAccurate(userDetails);

        System.out.println("Signing in");
        //Check the username and password against the data in Database
        //Should use an encrypted password while matching against the rows in a DB. Would be ideal if we are able to send an ecrypted password
        return "";
    }

    private boolean checkIfCredentialsAreAccurate(UserDetails userDetails) {
        String enteredUsername = userDetails.getUserName();
        String enteredPassword = userDetails.getPassword();

        // searches for the 1 unique user
        int searchForUser = 1;//repository.findOne(new Query(where("mUserName").is(enteredUsername)), UserDetails.class);
        // int searchForUser = repository.find(
        // {"mUserName": enteredUsername, "mPassword": enteredPassword}
        //.toArray()[0].length; // may need to index into array to get proper count

        if(searchForUser == 1)
            return true; // there exists a unique user w/ matching credentials
        else
            return false; // user not found in database
    }

}
