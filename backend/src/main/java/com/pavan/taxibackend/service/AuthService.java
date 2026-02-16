package com.pavan.taxibackend.service;

import com.pavan.taxibackend.dto.LoginRequest;
import com.pavan.taxibackend.dto.LoginResponse;
import com.pavan.taxibackend.model.User;
import com.pavan.taxibackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Authenticate user with phone + password and return profile data.
     */
    public LoginResponse login(LoginRequest request) {
        String phoneNumber = request.getPhoneNumber();

        // Find user by phone
        Optional<User> userOpt = userRepository.findByPhone(phoneNumber);
        if (userOpt.isEmpty()) {
            return new LoginResponse(false, "Invalid phone number or password");
        }

        User user = userOpt.get();

        // Check phone is verified
        if (!user.getIsPhoneVerified()) {
            return new LoginResponse(false, "Phone number not verified. Please register first.");
        }

        // Check password is set
        if (user.getPassword() == null) {
            return new LoginResponse(false, "Account setup incomplete. Please complete registration.");
        }

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new LoginResponse(false, "Invalid phone number or password");
        }

        // Build profile picture URL
        String profilePictureUrl = null;
        if (user.getProfilePicturePath() != null) {
            // Convert filesystem path to URL path
            profilePictureUrl = "/" + user.getProfilePicturePath().replace("\\", "/");
        }

        return new LoginResponse(
                true,
                "Login successful",
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                profilePictureUrl);
    }
}
