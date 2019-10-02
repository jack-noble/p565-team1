package com.infinitycare.health.login.controller;

import com.infinitycare.health.login.model.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @RequestMapping("/")
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("login.html");
        return mv;
    }

    @RequestMapping("LoggedIn")
    public ModelAndView login(UserDetails user) {
        ModelAndView mv = new ModelAndView();

        //Check the username and password against the data in Database
        //Should use an encrypted password while matching against the rows in a DB. Would be ideal if we are able to send an ecrypted password
        mv.addObject("username", user.getUserName());
        mv.setViewName("LoggedIn.html");
        return mv;
    }
}
