package com.cdw.helper.app.common.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 *  Custom exception entity for Helper application
 */
@Data
@AllArgsConstructor
public class HelperAppException extends RuntimeException {
    private HttpStatus status;
    private String message;
}
