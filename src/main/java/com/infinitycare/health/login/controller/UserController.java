package com.infinitycare.health.login.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.infinitycare.health.login.model.CookieDetails;
import com.infinitycare.health.login.service.*;
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
    AppointmentsService appointmentsService;

    @Inject
    public UserController(LoginService loginService, SignUpService signupservice, OtpService otpservice, PasswordRecoveryService passwordRecoveryService, AppointmentsService appointmentsService){
        this.loginService = loginService;
        this.signupservice = signupservice;
        this.otpservice = otpservice;
        this.passwordRecoveryService = passwordRecoveryService;
        this.appointmentsService = appointmentsService;
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

    @GetMapping(value = "/recovery/{userType}")
    public ResponseEntity<?> recoverPassword(HttpServletRequest request, @PathVariable String userType) {
        return this.passwordRecoveryService.setPassword(request, userType);
    }

    @RequestMapping(value = "/{userType}/gettimeslots")
    public ResponseEntity<?> getTimeSlots(HttpServletRequest request) {
        return this.appointmentsService.getTimeSlots(request);
    }

    @RequestMapping(value = "/{userType}/createappointments")
    public ResponseEntity<?> createAppointment(HttpServletRequest request) throws JsonProcessingException {
        return this.appointmentsService.createAppointments(request);
    }

    @RequestMapping(value = "/{userType}/getappointments")
    public ResponseEntity<?> getAppointments(HttpServletRequest request, @PathVariable String userType) {
        return this.appointmentsService.getAppointments(request, userType);
    }

    @RequestMapping(value = "/{userType}/cancelappointments")
    public ResponseEntity<?> deleteAppointments(HttpServletRequest request, @PathVariable String userType) {
        return this.appointmentsService.cancelAppointments(request, userType);
    }

}
