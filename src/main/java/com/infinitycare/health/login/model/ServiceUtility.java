package com.infinitycare.health.login.model;

import com.infinitycare.health.database.IpRepository;
import com.infinitycare.health.login.SendEmailSMTP;
import com.infinitycare.health.security.TextSecurer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServiceUtility {

    public static final String EMAIL_ID = "emailID";

    //Common for all users
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String FIRSTNAME = "firstName";
    public static final String LASTNAME = "lastName";
    public static final String ADDRESS = "address";

    //Patient Details
    public static final String DOB = "dob";

    //Doctor details
    public static final String HOSPITAL = "hospital";
    public static final String SPECIALIZATION = "specialization";

    //Insurance Provider details
    public static final String PHONENUMBER = "phoneNumber";
    public static final String COMPANY = "company";


    public static final String OTP = "otp";
    public static final String SESSIONID = "sessionid";

    public static final String PATIENT = "patient";
    public static final String DOCTOR = "doctor";
    public static final String INSURANCE_PROVIDER = "insurance";
    public static final String IS_CREDENTIALS_ACCURATE = "isCredentialsAccurate";
    public static final String IS_OTP_SENT = "isOtpSent";
    public static final String IS_OTP_ACCURATE = "isOtpAccurate";
    public static final String IS_COOKIE_TAMPERED = "isCookieTampered";
    public static final String IS_NEW_USER = "isNewUser";
    public static final String IS_USER_EXISTS = "isUserExists";
    public static final String IS_PASSWORD_CHANGED = "isPasswordChanged";

    public static final String FRONTEND_BASE_URL = "https://infinity-care.herokuapp.com";
    public static final String CONSULTATION_FEE = "consultationFee";
    //public static final String FRONTEND_BASE_URL = "http://localhost:3000";

    public Map<String, String> getPostBodyInAMap(HttpServletRequest request) {
        Map<String, String> postBody = new HashMap<>();
        try {
            populatePostBody(postBody, request.getReader().lines().collect(Collectors.joining()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return postBody;
    }

    private void populatePostBody(Map<String, String> parameterMap, String body) {
        try {
            JSONObject userDetails = (JSONObject) new JSONParser().parse(body);
            for(Object key: userDetails.keySet()) {
                parameterMap.put((String)key, (String)userDetails.get(key));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getUsername(HttpServletRequest request) {
        return getSessionIdentifier(request, true);
    }

    public String getSessionIdentifier(HttpServletRequest request) {
        return getSessionIdentifier(request, false);
    }

    private String getSessionIdentifier(HttpServletRequest request, boolean shouldDecryptTheIdentifier) {
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies) {
            if(SESSIONID.equals(c.getName())) {
                return shouldDecryptTheIdentifier ? TextSecurer.decrypt(c.getValue()) : c.getValue();
            }
        }

        return null;
    }

    public void setSessionId(HttpServletRequest request, HttpServletResponse response, String username, String userType, int maxAge) {
        String encryptedSessionId = username;
        String servletPath = request.getServletPath();
        String cookiePath = servletPath.substring(0, servletPath.indexOf(userType) + userType.length());

        Cookie cookie = new Cookie(SESSIONID, encryptedSessionId);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        cookie.setPath(cookiePath);

        //response.setHeader("Set-Cookie", SESSIONID + "=" + encryptedSessionId + "; Path=" + cookiePath + "; HttpOnly; SameSite=Lax");

        response.addCookie(cookie);
    }

    public void sendLoginOTP(String username, String otp) {
        String emailBody = "";
        emailBody += "<h1>" + "InfinityCare" + "</h1>\n\n" + "<h2> Please enter the OTP when prompted </h2>\n"
                + "<h3>" + "OTP: " + otp + "</h3>\n";
        SendEmailSMTP.sendFromGMail(new String[]{username}, "Login Authorization", emailBody);
    }

    public IPDetails getDetailsOfInsuranceProviderWhoCreatedThePlan(IpRepository ipRepository, String planName) {
        List<IPDetails> insuranceProviders = ipRepository.findAll();
        IPDetails result = null;

        for (IPDetails insuranceProvider : insuranceProviders) {
            if(insuranceProvider.getIpPlans().contains(planName)) {
                result = insuranceProvider;
                break;
            }
        }

        return result;
    }

}
