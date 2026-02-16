package com.pavan.taxibackend.service;

import com.pavan.taxibackend.dto.RegisterPhoneRequest;
import com.pavan.taxibackend.dto.RegistrationResponse;
import com.pavan.taxibackend.dto.SetPasswordRequest;
import com.pavan.taxibackend.dto.VerifyOtpRequest;
import com.pavan.taxibackend.model.User;
import com.pavan.taxibackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpService otpService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Step 1: Register phone number and generate OTP
     */
    public RegistrationResponse registerPhone(RegisterPhoneRequest request) {
        String phoneNumber = request.getPhoneNumber();

        // Check if phone already exists and is verified
        Optional<User> existingUser = userRepository.findByPhone(phoneNumber);
        if (existingUser.isPresent() && existingUser.get().getIsPhoneVerified()) {
            return new RegistrationResponse(false, "Phone number already registered");
        }

        // Generate OTP
        String otp = otpService.generateOtp(phoneNumber);

        // Create or update user
        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            user = new User();
            user.setPhone(phoneNumber);
            user.setIsPhoneVerified(false);
            user.setIsActive(true);
            userRepository.save(user);
        }

        return new RegistrationResponse(true, "OTP sent successfully");
    }

    /**
     * Step 2: Verify OTP
     */
    public RegistrationResponse verifyPhoneOtp(VerifyOtpRequest request) {
        String phoneNumber = request.getPhoneNumber();
        String otp = request.getOtp();

        // Verify OTP
        boolean isValid = otpService.verifyOtp(phoneNumber, otp);
        if (!isValid) {
            return new RegistrationResponse(false, "Invalid or expired OTP");
        }

        // Find user and mark phone as verified
        Optional<User> userOpt = userRepository.findByPhone(phoneNumber);
        if (userOpt.isEmpty()) {
            return new RegistrationResponse(false, "User not found");
        }

        User user = userOpt.get();
        user.setIsPhoneVerified(true);
        userRepository.save(user);

        return new RegistrationResponse(true, "Phone verified successfully");
    }

    /**
     * Step 3: Set password
     */
    public RegistrationResponse setPassword(SetPasswordRequest request) {
        String phoneNumber = request.getPhoneNumber();
        String password = request.getPassword();
        String confirmPassword = request.getConfirmPassword();

        // Validate passwords match
        if (!password.equals(confirmPassword)) {
            return new RegistrationResponse(false, "Passwords do not match");
        }

        // Find user
        Optional<User> userOpt = userRepository.findByPhone(phoneNumber);
        if (userOpt.isEmpty()) {
            return new RegistrationResponse(false, "User not found");
        }

        User user = userOpt.get();

        // Check if phone is verified
        if (!user.getIsPhoneVerified()) {
            return new RegistrationResponse(false, "Phone number not verified");
        }

        // Encrypt and save password
        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        userRepository.save(user);

        return new RegistrationResponse(true, "Registration completed successfully");
    }
}
