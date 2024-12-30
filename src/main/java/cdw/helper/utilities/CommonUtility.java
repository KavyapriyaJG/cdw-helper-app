package cdw.helper.utilities;

import cdw.helper.constants.HelperAppConstants;
import cdw.helper.entities.RoleEnum;
import cdw.helper.exceptions.HelperAppException;
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
