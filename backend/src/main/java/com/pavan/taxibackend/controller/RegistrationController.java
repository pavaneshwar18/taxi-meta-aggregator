package com.pavan.taxibackend.controller;

import com.pavan.taxibackend.dto.RegisterPhoneRequest;
import com.pavan.taxibackend.dto.RegistrationResponse;
import com.pavan.taxibackend.dto.SetPasswordRequest;
import com.pavan.taxibackend.dto.VerifyOtpRequest;
import com.pavan.taxibackend.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/register")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    /**
     * Step 1: Register phone number and send OTP
     */
    @PostMapping("/phone")
    public ResponseEntity<RegistrationResponse> registerPhone(@Valid @RequestBody RegisterPhoneRequest request) {
        RegistrationResponse response = registrationService.registerPhone(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Step 2: Verify OTP
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<RegistrationResponse> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        RegistrationResponse response = registrationService.verifyPhoneOtp(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Step 3: Set password after OTP verification
     */
    @PostMapping("/set-password")
    public ResponseEntity<RegistrationResponse> setPassword(@Valid @RequestBody SetPasswordRequest request) {
        RegistrationResponse response = registrationService.setPassword(request);
        return ResponseEntity.ok(response);
    }
}
