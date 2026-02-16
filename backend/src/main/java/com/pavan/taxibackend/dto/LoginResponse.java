package com.pavan.taxibackend.dto;

public class LoginResponse {

    private boolean success;
    private String message;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String profilePictureUrl;

    public LoginResponse() {
    }

    // Error response
    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Success response with profile data
    public LoginResponse(boolean success, String message, String firstName, String lastName,
            String email, String phone, String profilePictureUrl) {
        this.success = success;
        this.message = message;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.profilePictureUrl = profilePictureUrl;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
