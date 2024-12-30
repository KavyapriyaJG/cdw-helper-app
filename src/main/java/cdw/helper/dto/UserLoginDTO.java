package cdw.helper.dto;

import lombok.Data;

/**
 * Data transfer object for user login details
 */
@Data
public class UserLoginDTO {
    private String emailAddress;
    private String password;
}
