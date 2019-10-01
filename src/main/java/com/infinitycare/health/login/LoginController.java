package com.infinitycare.health.login;

import com.infinitycare.health.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @RequestMapping("LoggedIn")
    public ModelAndView login(UserDetails user) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("username", user.getUserName());
        mv.setViewName("LoggedIn.html");
        return mv;
    }
}
