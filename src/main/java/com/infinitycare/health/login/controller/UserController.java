package com.infinitycare.health.login.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.infinitycare.health.login.model.ServiceUtility;
import com.infinitycare.health.login.service.*;
import com.infinitycare.health.search.Search;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/")
@CrossOrigin(origins = "*")
public class UserController extends ServiceUtility {

    LoginService loginService;
    SignUpService signupservice;
    OtpService otpservice;
    ForgotPasswordService forgotPasswordService;
    AppointmentsService appointmentsService;
    Search search;

    @Inject
    public UserController(LoginService loginService, SignUpService signupservice, OtpService otpservice, ForgotPasswordService forgotPasswordService, AppointmentsService appointmentsService){
        this.loginService = loginService;
        this.signupservice = signupservice;
        this.otpservice = otpservice;
        this.forgotPasswordService = forgotPasswordService;
        this.appointmentsService = appointmentsService;
    }

    @RequestMapping(value = "/{userType}/login", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<?> validateUser(HttpServletRequest request, HttpServletResponse response, @PathVariable String userType) {
        return this.loginService.validateCredentials(request, response, userType);
    }

    @RequestMapping(value = "/{userType}/signup", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<?> signupUser(HttpServletRequest request, HttpServletResponse response, @PathVariable String userType) {
        return this.signupservice.signup(request, response, userType);
    }

    @RequestMapping(value = "/{userType}/mfa", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<?> enterOtp(HttpServletRequest request, @PathVariable String userType) {
        return this.otpservice.validateOtp(request, userType);
    }

    @RequestMapping(value = "/{userType}/forgotpassword")
    public ResponseEntity<?> recoverPassword(HttpServletRequest request, @PathVariable String userType) {
        return this.forgotPasswordService.setPassword(request, userType);
    }

    @RequestMapping(value = "/{userType}/forgotpassword/email")
    public ResponseEntity<?> verifyEmail(HttpServletRequest request, @PathVariable String userType) {
        return this.forgotPasswordService.verifyUsername(request, userType);
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

    @RequestMapping(value = "/{userType}/search")
    public ResponseEntity<?> searchForUsers(HttpServletRequest request, @PathVariable String userType, @RequestParam("otp") String userName) {
        return search.searchForUsers(request, userType, userName);
    }

}
