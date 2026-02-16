package com.pavan.taxibackend.service;

import com.pavan.taxibackend.dto.RegisterPhoneRequest;
import com.pavan.taxibackend.dto.RegistrationResponse;
import com.pavan.taxibackend.dto.SetPasswordRequest;
import com.pavan.taxibackend.dto.UserProfileRequest;
import com.pavan.taxibackend.dto.VerifyOtpRequest;
import com.pavan.taxibackend.model.User;
import com.pavan.taxibackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class RegistrationService {

    private static final String UPLOAD_DIR = "uploads/profile-pictures";

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

        return new RegistrationResponse(true, "Password set successfully");
    }

    /**
     * Step 4: Update user profile (first name, last name, email, profile picture)
     */
    public RegistrationResponse updateProfile(UserProfileRequest request, MultipartFile profilePicture) {
        String phoneNumber = request.getPhoneNumber();

        // Find user
        Optional<User> userOpt = userRepository.findByPhone(phoneNumber);
        if (userOpt.isEmpty()) {
            return new RegistrationResponse(false, "User not found");
        }

        User user = userOpt.get();

        // Ensure user has completed password setup
        if (user.getPassword() == null) {
            return new RegistrationResponse(false, "Please set your password first");
        }

        // Set profile details
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            user.setEmail(request.getEmail());
        }

        // Handle profile picture upload
        if (profilePicture != null && !profilePicture.isEmpty()) {
            try {
                String filePath = saveProfilePicture(profilePicture, user.getId());
                user.setProfilePicturePath(filePath);
            } catch (IOException e) {
                return new RegistrationResponse(false, "Failed to upload profile picture");
            }
        }

        userRepository.save(user);
        return new RegistrationResponse(true, "Profile updated successfully");
    }

    /**
     * Save profile picture to disk and return the relative path.
     */
    private String saveProfilePicture(MultipartFile file, Long userId) throws IOException {
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename: userId_uuid.extension
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String filename = userId + "_" + UUID.randomUUID().toString() + extension;

        // Save file
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath);

        return filePath.toString();
    }
}
