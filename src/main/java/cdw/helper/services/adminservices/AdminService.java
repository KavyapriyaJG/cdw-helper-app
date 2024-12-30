package cdw.helper.services.adminservices;

import cdw.helper.dto.SuccessResponseDTO;
import cdw.helper.dto.UserUpdationDTO;
import cdw.helper.entities.User;

import java.util.List;

/**
 * Interface for Admin Service operations
 */
public interface AdminService {
    List<User> fetchAllUsers();

    String verifyUser(Long id);

    void updateUser(Long id,UserUpdationDTO userUpdationDTO);

    void deleteUser(Long id);
}
