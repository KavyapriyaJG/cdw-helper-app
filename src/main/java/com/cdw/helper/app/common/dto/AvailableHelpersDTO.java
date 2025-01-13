package com.cdw.helper.app.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for the sending available helpers in response
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableHelpersDTO {
    Long id;
    String firstName;
    String lastName;
    String specialization;
    float hourlyRate;
    float ratings;
}
