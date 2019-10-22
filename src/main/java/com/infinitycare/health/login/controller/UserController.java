package com.infinitycare.health.login.controller;

import com.infinitycare.health.login.model.CookieDetails;
import com.infinitycare.health.login.service.LoginService;
import com.infinitycare.health.login.service.OtpService;
import com.infinitycare.health.login.service.PasswordRecoveryService;
import com.infinitycare.health.login.service.SignUpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.inject.Inject;

@RestController
@RequestMapping(value = "/")
public class UserController extends CookieDetails {

    LoginService loginService;
    SignUpService signupservice;
    OtpService otpservice;
    PasswordRecoveryService passwordRecoveryService;

    @Inject
    public UserController(LoginService loginService, SignUpService signupservice, OtpService otpservice, PasswordRecoveryService passwordRecoveryService){
        this.loginService = loginService;
        this.signupservice = signupservice;
        this.otpservice = otpservice;
        this.passwordRecoveryService = passwordRecoveryService;
    }

    @GetMapping(value = "/login/{userType}")
    public ResponseEntity<?> validateUser(HttpServletRequest request, HttpServletResponse response, @PathVariable String userType) {
        return this.loginService.validateCredentials(request, response, userType);
    }

    @GetMapping(value = "/signup/{userType}")
    public ResponseEntity<?> signupUser(HttpServletRequest request, HttpServletResponse response, @PathVariable String userType) {
        return this.signupservice.signup(request, response, userType);
    }

    @GetMapping(value = "/otp/{userType}")
    public ResponseEntity<?> enterOtp(HttpServletRequest request, @PathVariable String userType, @RequestParam("otp") String enteredotp) {
        return this.otpservice.validateOtp(request, userType, enteredotp);
    }

    @GetMapping(value = "/recoverpassword/{userType}")
    public ResponseEntity<?> recoverPassword(HttpServletRequest request, @PathVariable String userType) {
        return this.passwordRecoveryService.setPassword(request, userType);
    }

}
