package com.infinitycare.health.login.service;

import com.infinitycare.health.login.model.ServiceUtility;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Service
public class SignOutService extends ServiceUtility {

    public ResponseEntity<?> signOut(HttpServletRequest request, HttpServletResponse response, String userType) {
        Map<String, Object> result = new HashMap<>();
        result.put("isSessionIdDestroyed", "true");
        setSessionId(request, response, getSessionIdentifier(request), userType, 0);
        return ResponseEntity.ok(result);
    }
}
