package cdw.helper.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Data transfer Object for sending success responses
 */
@Data
public class SuccessResponseDTO {
    private HttpStatus status;
    private Object body;
}
