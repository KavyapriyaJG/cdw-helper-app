package cdw.helper.handlers;

import cdw.helper.dto.FailureResponseDTO;
import cdw.helper.exceptions.HelperAppException;
import cdw.helper.exceptions.ResourceNotFoundException;
import cdw.helper.utilities.ExceptionUtility;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

/**
 * GlobalExceptionHandler handles all exception thrown from the Helper App controllers
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handles resource not found exceptions
     * @param ex
     * @return ResponseEntity
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {

        FailureResponseDTO failureResponseDTO = new FailureResponseDTO();
        failureResponseDTO.setStatus(ex.getStatus());
        failureResponseDTO.setMessage(ex.getMessage());

        return new ResponseEntity<>(failureResponseDTO, ex.getStatus());
    }

    /**
     * Handles bad request exceptions
     * @param ex
     * @return ResponseEntity
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<FailureResponseDTO> handleBadRequestException(BadRequestException ex) {

        FailureResponseDTO failureResponseDTO = new FailureResponseDTO();
        failureResponseDTO.setStatus(HttpStatus.BAD_REQUEST);
        failureResponseDTO.setMessage(ex.getMessage());

        return new ResponseEntity<>(failureResponseDTO, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles HelperApp custom exceptions that are thrown
     * @param ex
     * @return ResponseEntity
     */
    @ExceptionHandler(HelperAppException.class)
    public ResponseEntity<Object> handleGatekeeperException(HelperAppException ex) throws IOException {

        ExceptionUtility exceptionUtility = new ExceptionUtility();
        FailureResponseDTO  failureResponseDTO = new FailureResponseDTO();

        failureResponseDTO.setStatus(ex.getStatus());
        failureResponseDTO.setMessage(exceptionUtility.getPropertyCode(ex.getMessage()));

        return new ResponseEntity<>(failureResponseDTO, ex.getStatus());
    }

    /**
     * Handles all other generic exceptions thrown
     * @param ex
     * @return ResponseEntity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<FailureResponseDTO> handleExceptions(Exception ex) {
        FailureResponseDTO failureResponseDTO = new FailureResponseDTO();
        failureResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        failureResponseDTO.setMessage("OOPS something went wrong! Internal server error");

        return new ResponseEntity<>(failureResponseDTO, failureResponseDTO.getStatus());
    }
}
