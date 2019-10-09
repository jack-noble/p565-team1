package com.infinitycare.health.login.controller;

import com.infinitycare.health.database.UserRepository;
import com.infinitycare.health.login.SendEmailSMTP;
import com.infinitycare.health.login.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

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

        System.out.println("Signing in");
        boolean isAccurateCredentials = checkIfCredentialsAreAccurate(userDetails);

        if(!isAccurateCredentials)
            return "isCredentialsCorrect:" + isAccurateCredentials;

        SendEmailSMTP.sendFromGMail(new String[]{request.getParameter("username")}, "Please enter the OTP in the login screen", SendEmailSMTP.generateRandomNumber(1000, 9999));
        //Check the username and password against the data in Database
        //Should use an encrypted password while matching against the rows in a DB. Would be ideal if we are able to send an ecrypted password
        return "isCredentialsCorrect:" + isAccurateCredentials;
    }

    private boolean checkIfCredentialsAreAccurate(UserDetails userDetails) {
        String enteredUsername = userDetails.getUserName();
        String enteredPassword = userDetails.getPassword();

        Optional<UserDetails> userQueriedFromDB = repository.findById(Integer.toString(enteredUsername.hashCode()));

        if(userQueriedFromDB.isPresent()) {
            return userQueriedFromDB.get().getPassword().equals(enteredPassword);
        } else {
            return false; // user not found in database
        }
    }

}
