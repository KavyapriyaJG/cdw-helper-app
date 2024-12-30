package cdw.helper.dto;

import lombok.Data;

/**
 * Data transfer object for appointment entity
 */
@Data
public class AppointmentDTO {
    private String scheduledAt;
    private Long helperId;
}
