package com.infinitycare.health.login.controller;

import com.infinitycare.health.login.model.CookieDetails;
import com.infinitycare.health.login.service.LoginService;
import com.infinitycare.health.login.service.OtpService;
import com.infinitycare.health.login.service.SignUpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.inject.Inject;

@RestController
@RequestMapping(value = "/")
@CrossOrigin(origins = "*")
public class UserController extends CookieDetails {

    LoginService loginService;
    SignUpService signupservice;
    OtpService otpservice;

    @Inject
    public UserController(LoginService loginService, SignUpService signupservice, OtpService otpservice){
        this.loginService = loginService;
        this.signupservice = signupservice;
        this.otpservice = otpservice;
    }

    @RequestMapping(value = "/login/{userType}", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    public ResponseEntity<?> validateUser(HttpServletRequest request, HttpServletResponse response, @PathVariable String userType) {
        return this.loginService.validateCredentials(request, response, userType);
    }

    @RequestMapping(value = "/signup/{userType}", method = RequestMethod.POST)
    public ResponseEntity<?> signupUser(HttpServletRequest request, HttpServletResponse response, @PathVariable String userType) {
        return this.signupservice.signup(request, response, userType);
    }

    @RequestMapping(value = "/otp/{userType}", method = RequestMethod.POST)
    public ResponseEntity<?> enterOtp(HttpServletRequest request, @PathVariable String userType, @RequestParam("otp") String enteredotp) {
        return this.otpservice.validateOtp(request, userType, enteredotp);
    }

}
