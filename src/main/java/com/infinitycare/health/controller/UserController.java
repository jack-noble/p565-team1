package com.infinitycare.health.controller;

import com.infinitycare.health.billing.BillingService;
import com.infinitycare.health.login.model.ServiceUtility;
import com.infinitycare.health.login.service.*;
import com.infinitycare.health.recommendations.RecommendationsService;
import com.infinitycare.health.search.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

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
    SearchService searchService;
    ProfileService profileService;
    SignOutService signOutService;
    RecommendationsService recommendationsService;
    BillingService billingService;

    @Inject
    public UserController(LoginService loginService, SignUpService signupservice, OtpService otpservice, ForgotPasswordService forgotPasswordService,
                          AppointmentsService appointmentsService, ProfileService profileService, SearchService searchService, DashboardService dashboardService,
                          SignOutService signOutService, RecommendationsService recommendationsService, BillingService billingService){
        this.loginService = loginService;
        this.signupservice = signupservice;
        this.otpservice = otpservice;
        this.forgotPasswordService = forgotPasswordService;
        this.appointmentsService = appointmentsService;
        this.dashboardService = dashboardService;
        this.profileService = profileService;
        this.searchService = searchService;
        this.signOutService = signOutService;
        this.recommendationsService = recommendationsService;
        this.billingService = billingService;
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

    @RequestMapping(value = "/{userType}/forgotpassword")
    public ResponseEntity<?> recoverPassword(HttpServletRequest request, @PathVariable String userType) {
        return this.forgotPasswordService.setPassword(request, userType);
    }

    @RequestMapping(value = "/{userType}/forgotpassword/email")
    public ResponseEntity<?> verifyEmail(HttpServletRequest request, HttpServletResponse response, @PathVariable String userType) {
        return this.forgotPasswordService.verifyUsername(request, response, userType);
    }

    @RequestMapping(value = "/doctor/time/{doctorusername}")
    public ResponseEntity<?> getTimeSlots(HttpServletRequest request, @PathVariable String doctorusername) {
        doctorusername = new String(Base64.getDecoder().decode(doctorusername));
        return this.appointmentsService.getTimeSlots(request, doctorusername);
    }

    @RequestMapping(value = "/patient/createappointments")
    public ResponseEntity<?> createAppointment(HttpServletRequest request) {
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
    public ResponseEntity<?> deleteAppointments(HttpServletRequest request, @PathVariable String userType) {
        return this.appointmentsService.cancelAppointments(request, userType);
    }

    @RequestMapping(value = "/{userType}/search")
    public ResponseEntity<?> searchForUsers(@PathVariable String userType, @RequestParam("query") String userName) {
        return searchService.searchForUsers(userType, userName);
    }

    @RequestMapping(value = "/{userType}/search/locations")
    public ResponseEntity<?> getLocationsOfQueriesUsers(@PathVariable String userType, @RequestParam("query") String userName) {
        return searchService.getLocations(userType, userName);
    }

    @RequestMapping(value = "/{userType}/signout")
    public ResponseEntity<?> signOut(HttpServletRequest request, HttpServletResponse response, @PathVariable String userType) {
        return signOutService.signOut(request, response, userType);
    }

    @RequestMapping(value = "/{userType}/profile")
    public ResponseEntity<?> getUserProfile(HttpServletRequest request, @PathVariable String userType) {
        return this.profileService.getProfile(request, userType);
    }

    @RequestMapping(value = "/patient/doctor/{doctorusername}")
    public ResponseEntity<?> getDoctorFromPatient(@PathVariable String doctorusername) {
        doctorusername = new String(Base64.getDecoder().decode(doctorusername));
        return this.profileService.getDoctorFromPatient(doctorusername);
    }

    @RequestMapping(value = "/{userType}/patient/{patientusername}")
    public ResponseEntity<?> getPatientFromOthers(@PathVariable String userType, @PathVariable String patientusername) {
        patientusername = new String(Base64.getDecoder().decode(patientusername));
        if(userType.equals(DOCTOR) || userType.equals(INSURANCE_PROVIDER)) {
            return this.profileService.getPatientFromOthers(patientusername);
        }
        else { return null; }
    }

    @RequestMapping(value = "/patient/insurance/{ipusername}")
    public ResponseEntity<?> getInsuranceFromPatient(@PathVariable String ipusername) {
        ipusername = new String(Base64.getDecoder().decode(ipusername));
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

    @RequestMapping(value = "/patient/profile/update")
    public ResponseEntity<?> updatePatientProfile(HttpServletRequest request) {
        return this.profileService.updatePatientProfile(request);
    }

    @RequestMapping(value = "/doctor/profile/update")
    public ResponseEntity<?> updateDoctorProfile(HttpServletRequest request) {
        return this.profileService.updateDoctorProfile(request);
    }

    @RequestMapping(value = "/insurance/profile/update")
    public ResponseEntity<?> updateIpProfile(HttpServletRequest request) {
        return this.profileService.updateIpProfile(request);
    }

    @RequestMapping(value = "/insurance/getpatients")
    public ResponseEntity<?> getPatients(HttpServletRequest request) {
        return this.dashboardService.getPatientsListForIp(request);
    }

    @RequestMapping(value = "/patient/survey")
    public ResponseEntity<?> saveRecommendationWeights(HttpServletRequest request) {
        return this.recommendationsService.saveRecommendationWeights(request);
    }

    @RequestMapping(value = "/patient/survey/results")
    public ResponseEntity<?> getRecommendations(HttpServletRequest request) {
        return this.recommendationsService.getRecommendations(request);
    }

    @RequestMapping(value = "/patient/insuranceplan")
    public ResponseEntity<?> updateInsurancePlanOfPatient(HttpServletRequest request) {
        return this.recommendationsService.updateInsurancePlanOfThePatient(request);
    }

    @RequestMapping(value = "/patient/insuranceplan/{planName}")
    public ResponseEntity<?> getInsurancePlanDetails(@PathVariable String planName) {
        planName = new String(Base64.getDecoder().decode(planName));
        return this.recommendationsService.getPlanDetails(planName);
    }

    @RequestMapping(value = "/patient/doctor/{doctorId}/addreviews")
    public ResponseEntity addReviews(HttpServletRequest request, @PathVariable String doctorId) {
        doctorId = new String(Base64.getDecoder().decode(doctorId));
        return this.dashboardService.addReviewsForDoctor(request, doctorId);
    }

    @RequestMapping(value = "/insurance/claims")
    public ResponseEntity<?> getInsuranceProviderClaims(HttpServletRequest request) {
        return this.billingService.getInsuranceProviderClaims(request);
    }

    @RequestMapping(value = "/insurance/editclaims/{billStatus}")
    public ResponseEntity<?> EditInsuranceProviderClaims(HttpServletRequest request, @PathVariable String billStatus) {
        return this.billingService.editInsuranceProviderBillStatus(request, billStatus);
    }

    @RequestMapping(value = "/patient/bills/history")
    public ResponseEntity<?> getPatientBillHistory(HttpServletRequest request) {
        return this.billingService.getPatientPaidBills(request);
    }

    @RequestMapping(value = "patient/bills/pay")
    public ResponseEntity<?> editPatientBillStatus(HttpServletRequest request) {
        return this.billingService.editPatientBillStatus(request, true);
    }

    @RequestMapping(value = "patient/claims")
    public ResponseEntity<?> getAllPatientClaims(HttpServletRequest request) {
        return this.billingService.getAllPatientClaims(request);
    }

}
