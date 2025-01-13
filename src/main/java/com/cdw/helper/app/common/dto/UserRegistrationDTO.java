package com.cdw.helper.app.common.dto;

import lombok.Data;

/**
 * Data Transfer Object Class for user registration details
 */
@Data
public class UserRegistrationDTO {
    // Admin and common for all users
    private String emailAddress;
    private String password;
    private String firstName;
    private String lastName;
    private String dob;
    private String gender;

    // Helper
    private String specialization;
    private float rating;
    private float hourlyRate;

    //Resident
    private String apartmentNumber;
}
