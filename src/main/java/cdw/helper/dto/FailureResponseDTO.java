package cdw.helper.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Data transfer object for sending failure responses
 */
@Data
public class FailureResponseDTO {
    private HttpStatus status;
    private String message;
}
