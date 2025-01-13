package com.cdw.helper.app.common.dto;

import lombok.Data;

/**
 * Data transfer object to update user details
 */
@Data
public class UserUpdationDTO {
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
