package com.pavan.taxibackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${twilio.phone_number}")
    private String twilioPhoneNumber;

    private static final String OTP_PREFIX = "otp:";
    private static final int OTP_LENGTH = 6;
    private static final long OTP_VALIDITY_MINUTES = 5;

    /**
     * Generate a 6-digit OTP and store in Redis with 5-minute TTL
     */
    public String generateOtp(String phoneNumber) {
        String otp = generateRandomOtp();
        String key = OTP_PREFIX + phoneNumber;

        // Store OTP in Redis with 5-minute expiration
        redisTemplate.opsForValue().set(key, otp, OTP_VALIDITY_MINUTES, TimeUnit.MINUTES);

        // Send OTP via Twilio
        try {
            Message.creator(
                    new PhoneNumber(phoneNumber), // To
                    new PhoneNumber(twilioPhoneNumber), // From
                    "Your Taxi App Verification Code is: " + otp).create();
            System.out.println("Sent OTP to " + phoneNumber + " via Twilio");
        } catch (Exception e) {
            System.err.println("Failed to send SMS: " + e.getMessage());
            // Fallback for testing/debugging
            System.out.println("OTP for " + phoneNumber + ": " + otp);
        }

        return otp;
    }

    /**
     * Verify OTP and remove from Redis if valid
     */
    public boolean verifyOtp(String phoneNumber, String otp) {
        String key = OTP_PREFIX + phoneNumber;
        String storedOtp = redisTemplate.opsForValue().get(key);

        if (storedOtp != null && storedOtp.equals(otp)) {
            // OTP is valid, delete it from Redis
            redisTemplate.delete(key);
            return true;
        }

        return false;
    }

    /**
     * Invalidate OTP for a phone number (remove from Redis)
     */
    public void invalidateOtp(String phoneNumber) {
        String key = OTP_PREFIX + phoneNumber;
        redisTemplate.delete(key);
    }

    /**
     * Generate a random 6-digit OTP
     */
    private String generateRandomOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
