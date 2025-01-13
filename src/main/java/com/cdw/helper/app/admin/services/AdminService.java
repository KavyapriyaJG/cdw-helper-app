package com.cdw.helper.app.admin.services;

import com.cdw.helper.app.common.dto.UserUpdationDTO;
import com.cdw.helper.app.common.entities.User;

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
