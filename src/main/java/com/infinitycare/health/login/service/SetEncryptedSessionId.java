package com.infinitycare.health.login.service;

import com.infinitycare.health.login.model.CookieDetails;
import com.infinitycare.health.security.TextSecurer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class SetEncryptedSessionId extends CookieDetails {

    static void setEncryptedSessionId(HttpServletRequest request, HttpServletResponse response, String username, String userType) {
        String encryptedSessionId = TextSecurer.encrypt(username);
        String servletPath = request.getServletPath();
        String cookiePath = servletPath.substring(0, servletPath.indexOf(userType) + userType.length());

        Cookie cookie = new Cookie(SESSIONID, encryptedSessionId);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(-1);
        cookie.setPath(cookiePath);

        response.addCookie(cookie);
    }
}
