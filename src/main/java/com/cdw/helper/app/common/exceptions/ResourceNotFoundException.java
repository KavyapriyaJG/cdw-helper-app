package com.cdw.helper.app.common.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Custom Resource not found entity for Helper application
 */
public class ResourceNotFoundException extends HelperAppException {
    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
