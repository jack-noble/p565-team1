package com.infinitycare.health.login.controller;

import com.infinitycare.health.login.model.CookieDetails;
import com.infinitycare.health.login.service.LoginService;
import com.infinitycare.health.login.service.OtpService;
import com.infinitycare.health.login.service.SignUpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/")
public class UserController extends CookieDetails {

    @GetMapping(value = "/login/{userType}")
    public ResponseEntity<?> validateUser(HttpServletRequest request, HttpServletResponse response, @PathVariable String userType) {
        return new LoginService().validateCredentials(request, response, userType);
    }

    @GetMapping(value = "/signup/{userType}")
    public ResponseEntity<?> signupUser(HttpServletRequest request, HttpServletResponse response, @PathVariable String userType) {
        return new SignUpService().signup(request, response, userType);
    }

    @GetMapping(value = "/otp/{userType}")
    public ResponseEntity<?> enterOtp(HttpServletRequest request, @PathVariable String userType, @RequestParam("otp") String enteredotp) {
        return new OtpService().validateOtp(request, userType, enteredotp);
    }

}
