package com.cdw.helper.app.common.dto;

import lombok.Data;

/**
 * Data transfer object for appointment entity
 */
@Data
public class AppointmentDTO {
    private String scheduledAt;
    private Long helperId;
}
