package com.cdw.helper.app.common.dto;

import lombok.Data;

/**
 * Data transfer Object for sending success responses
 */
@Data
public class SuccessResponseDTO {
    private boolean success;
    private Object body;
}
