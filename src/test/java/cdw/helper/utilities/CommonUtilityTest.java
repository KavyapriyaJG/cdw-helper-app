package cdw.helper.utilities;

import cdw.helper.constants.HelperAppConstants;
import cdw.helper.entities.RoleEnum;
import cdw.helper.exceptions.HelperAppException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite that holds test cases for common utility methods
 */
public class CommonUtilityTest {

    @Test
    void roleStringProcess_ShouldReturnAdminRole_WhenInputIsAdmin() {
        String roleInput = "ADMIN";
        String result = CommonUtility.roleStringProcess(roleInput);
        assertEquals(RoleEnum.ROLE_ADMIN.toString(), result);
    }

    @Test
    void roleStringProcess_ShouldReturnResidentRole_WhenInputIsResident() {
        String roleInput = "RESIDENT";
        String result = CommonUtility.roleStringProcess(roleInput);
        assertEquals(RoleEnum.ROLE_RESIDENT.toString(), result);
    }

    @Test
    void roleStringProcess_ShouldReturnHelperRole_WhenInputIsHelper() {
        String roleInput = "HELPER";
        String result = CommonUtility.roleStringProcess(roleInput);
        assertEquals(RoleEnum.ROLE_HELPER.toString(), result);
    }

    @Test
    void roleStringProcess_ShouldThrowException_WhenInputIsInvalid() {
        String roleInput = "INVALID_ROLE";
        HelperAppException exception = assertThrows(HelperAppException.class, () -> {
            CommonUtility.roleStringProcess(roleInput);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals(HelperAppConstants.HA003, exception.getMessage());
    }
}
