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
@CrossOrigin(origins = "*")
public class UserController extends CookieDetails {

    LoginService loginService;
    SignUpService signupservice;
    OtpService otpservice;
    PasswordRecoveryService passwordRecoveryService;
    AppointmentsService appointmentsService;
    InsuranceDashboardService insuranceDashboardService;
    ProfileService profileService;

    @Inject
    public UserController(LoginService loginService, SignUpService signupservice, OtpService otpservice, PasswordRecoveryService passwordRecoveryService,
                          AppointmentsService appointmentsService, InsuranceDashboardService insuranceDashboardService, ProfileService profileService){
        this.loginService = loginService;
        this.signupservice = signupservice;
        this.otpservice = otpservice;
        this.passwordRecoveryService = passwordRecoveryService;
        this.appointmentsService = appointmentsService;
        this.insuranceDashboardService = insuranceDashboardService;
        this.profileService = profileService;
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

    @GetMapping(value = "/recovery/{userType}")
    public ResponseEntity<?> recoverPassword(HttpServletRequest request, @PathVariable String userType) {
        return this.passwordRecoveryService.setPassword(request, userType);
    }

    @RequestMapping(value = "/doctor/{doctorusername}")
    public ResponseEntity<?> getTimeSlots(HttpServletRequest request, @PathVariable String doctorusername) {
        return this.appointmentsService.getTimeSlots(request, doctorusername);
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

    @RequestMapping(value = "/{userType}/profile")
    public ResponseEntity<?> getUserProfile(HttpServletRequest request, @PathVariable String userType) {
        return this.profileService.getProfile(request, userType);
    }

    @RequestMapping(value = "/patient/doctor/{doctorusername}")
    public ResponseEntity<?> getDoctorFromPatient(@PathVariable String doctorusername) {
        return this.profileService.getDoctorFromPatient(doctorusername);
    }

    @RequestMapping(value = "/{userType}/patient/{patientusername}")
    public ResponseEntity<?> getPatientFromOthers(@PathVariable String userType, @PathVariable String patientusername) {
        if(userType.equals(DOCTOR) || userType.equals(INSURANCE_PROVIDER)) {
            return this.profileService.getPatientFromOthers(patientusername);
        }
        else { return null; }
    }

    @RequestMapping(value = "/patient/insurance/{ipusername}")
    public ResponseEntity<?> getInsuranceFromPatient(@PathVariable String ipusername) {
        return this.profileService.getIpFromPatient(ipusername);
    }

}
