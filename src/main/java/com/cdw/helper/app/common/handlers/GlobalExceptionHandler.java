package com.cdw.helper.app.common.handlers;

import com.cdw.helper.app.common.dto.FailureResponseDTO;
import com.cdw.helper.app.common.exceptions.HelperAppException;
import com.cdw.helper.app.common.exceptions.ResourceNotFoundException;
import com.cdw.helper.app.common.utilities.ExceptionUtility;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

/**
 * GlobalExceptionHandler handles all exception thrown from the Helper App controllers
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handles resource not found exceptions
     * @param ex
     * @return String
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ex.getMessage();
    }

    /**
     * Handles bad request exceptions
     * @param ex
     * @return String
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public String handleBadRequestException(BadRequestException ex) {
        return ex.getMessage();
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

        failureResponseDTO.setSuccess(false);
        failureResponseDTO.setMessage(exceptionUtility.getPropertyCode(ex.getMessage()));

        return new ResponseEntity<>(failureResponseDTO, ex.getStatus());
    }

    /**
     * Handles all other generic exceptions thrown
     * @param ex
     * @return String
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleExceptions(Exception ex) {
        return "OOPS something went wrong! Internal server error";
    }
}
