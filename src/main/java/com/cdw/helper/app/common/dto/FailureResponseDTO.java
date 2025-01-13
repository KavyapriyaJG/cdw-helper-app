package com.cdw.helper.app.common.dto;

import lombok.Data;

/**
 * Data transfer object for sending failure responses
 */
@Data
public class FailureResponseDTO {
    private boolean success;
    private String message;
}
