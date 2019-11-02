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
//@CrossOrigin(origins = "http://localhost:3000")
public class UserController extends ServiceUtility {

    LoginService loginService;
    SignUpService signupservice;
    OtpService otpservice;
    ForgotPasswordService forgotPasswordService;
    AppointmentsService appointmentsService;
    DashboardService dashboardService;
    Search search;
    ProfileService profileService;

    @Inject
    public UserController(LoginService loginService, SignUpService signupservice, OtpService otpservice, ForgotPasswordService forgotPasswordService,
                          AppointmentsService appointmentsService, ProfileService profileService, Search search, DashboardService dashboardService){
        this.loginService = loginService;
        this.signupservice = signupservice;
        this.otpservice = otpservice;
        this.forgotPasswordService = forgotPasswordService;
        this.appointmentsService = appointmentsService;
        this.dashboardService = dashboardService;
        this.profileService = profileService;
        this.search = search;
    }

    @RequestMapping(value = "/{userType}/login", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<?> validateUser(HttpServletRequest request, HttpServletResponse response, @PathVariable String userType) {
        return this.loginService.validateCredentials(request, response, userType);
    }

    @RequestMapping(value = "/{userType}/signup", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<?> signupUser(HttpServletRequest request, HttpServletResponse response, @PathVariable String userType) {
        return this.signupservice.signUp(request, response, userType);
    }

    @RequestMapping(value = "/{userType}/mfa", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<?> enterOtp(HttpServletRequest request, @PathVariable String userType) {
        return this.otpservice.validateOtp(request, userType);
    }

    //TODO: This is no longer required. Discuss with team and remove this tomorrow
    @RequestMapping(value = "/validateuser/{userType}")
    public ResponseEntity<?> validateUser(HttpServletRequest request, @PathVariable String userType) {
        return this.forgotPasswordService.validateUser(request, userType);
    }

    @RequestMapping(value = "/{userType}/forgotpassword")
    public ResponseEntity<?> recoverPassword(HttpServletRequest request, @PathVariable String userType) {
        return this.forgotPasswordService.setPassword(request, userType);
    }

    @RequestMapping(value = "/{userType}/forgotpassword/email")
    public ResponseEntity<?> verifyEmail(HttpServletRequest request, @PathVariable String userType) {
        return this.forgotPasswordService.verifyUsername(request, userType);
    }

    @RequestMapping(value = "/doctor/time/{doctorusername}")
    public ResponseEntity<?> getTimeSlots(HttpServletRequest request, @PathVariable String doctorusername) {
        return this.appointmentsService.getTimeSlots(request, doctorusername);
    }

    @RequestMapping(value = "/patient/createappointments")
    public ResponseEntity<?> createAppointment(HttpServletRequest request) throws JsonProcessingException {
        return this.appointmentsService.createAppointments(request);
    }

    @RequestMapping(value = "/{userType}/getappointments")
    public ResponseEntity<?> getAppointments(HttpServletRequest request, @PathVariable String userType) {
        return this.appointmentsService.getAppointments(request, userType);
    }

    @RequestMapping(value = "/{userType}/getpastappointments")
    public ResponseEntity<?> getPastAppointments(HttpServletRequest request, @PathVariable String userType) {
        return this.appointmentsService.getPastAppointments(request, userType);
    }

    @RequestMapping(value = "/{userType}/cancelappointments")
    public ResponseEntity<?> deleteAppointments(HttpServletRequest request, @PathVariable String userType) throws JsonProcessingException {
        return this.appointmentsService.cancelAppointments(request, userType);
    }

    @RequestMapping(value = "/{userType}/search")
    public ResponseEntity<?> searchForUsers(HttpServletRequest request, @PathVariable String userType, @RequestParam("username") String userName) {
        return search.searchForUsers(request, userType, userName);
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

    @RequestMapping(value = "/insurance/iplans")
    public ResponseEntity<?> getIplans(HttpServletRequest request) {
        return this.dashboardService.getIplans(request);
    }

    @RequestMapping(value = "/insurance/editiplans/{action}")
    public ResponseEntity<?> editIplans(HttpServletRequest request, @PathVariable String action) {
        return this.dashboardService.editIplans(request, action);
    }

    @RequestMapping(value = "/patient/editprofile/{section}")
    public ResponseEntity<?> editPatientProfile(HttpServletRequest request, @PathVariable String section) {
        return this.profileService.editPatientProfile(request, section);
    }

    @RequestMapping(value = "/doctor/editprofile/{section}")
    public ResponseEntity<?> editDoctorProfile(HttpServletRequest request, @PathVariable String section) {
        return this.profileService.editDoctorProfile(request, section);
    }

    @RequestMapping(value = "/insurance/editprofile")
    public ResponseEntity<?> editIpProfile(HttpServletRequest request) {
        return this.profileService.editIpProfile(request);
    }

    @RequestMapping(value = "/insurance/getpatients")
    public ResponseEntity<?> getPatients(HttpServletRequest request) {
        return this.dashboardService.getPatientsListForIp(request);
    }

    @RequestMapping(value = "/patient/doctor/addreviews")
    public ResponseEntity addReviews(HttpServletRequest request) {
        return this.dashboardService.addReviewsForDoctor(request);
    }

}
