package com.cdw.helper.app.common.utilities;

import com.cdw.helper.app.common.constants.HelperAppConstants;
import com.cdw.helper.app.common.entities.RoleEnum;
import com.cdw.helper.app.common.exceptions.HelperAppException;
import org.springframework.http.HttpStatus;

/**
 * CommonUtility encompasses general utilities for Helper App
 */
public class CommonUtility {
    /**
     * Pre-process the role string input from user
     * @param roleInput
     * @return
     */
    public static String roleStringProcess(String roleInput) {
        switch (roleInput.toUpperCase()) {
            case "ADMIN" -> {
                return RoleEnum.ROLE_ADMIN.toString();
            }
            case "RESIDENT" -> {
                return RoleEnum.ROLE_RESIDENT.toString();
            }
            case "HELPER" -> {
                return RoleEnum.ROLE_HELPER.toString();
            }
            default -> throw new HelperAppException(HttpStatus.CONFLICT, HelperAppConstants.HA003);
        }
    }

}
