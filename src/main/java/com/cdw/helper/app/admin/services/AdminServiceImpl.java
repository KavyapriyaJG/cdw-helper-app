package com.cdw.helper.app.admin.services;

import com.cdw.helper.app.common.constants.HelperAppConstants;
import com.cdw.helper.app.common.dto.UserUpdationDTO;
import com.cdw.helper.app.common.entities.User;
import com.cdw.helper.app.common.exceptions.HelperAppException;
import com.cdw.helper.app.common.exceptions.ResourceNotFoundException;
import com.cdw.helper.app.common.mappers.UserMapper;
import com.cdw.helper.app.common.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Class for Admin Service operations
 */
@Service
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Fetch all users
     * @return
     */
    @Override
    public List<User> fetchAllUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    /**
     * Checks the status of a user
     * @param userId
     * @return
     */
    @Transactional
    @Override
    public String verifyUser(Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new HelperAppException(HttpStatus.NOT_FOUND, HelperAppConstants.HA001));

            user.setApprovedByAdmin(true);
            userRepository.save(user);

            return "Approved Successfully !";
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Updates the user with data from the userUpdationDTO
     * @param userId
     * @param userUpdationDTO
     */
    @Transactional
    @Override
    public void updateUser(Long userId, UserUpdationDTO userUpdationDTO) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(()-> new ResourceNotFoundException(HelperAppConstants.HA001));

            user = userMapper.mapUpdatedUserToUserDTO(userUpdationDTO, user);
            userRepository.save(user);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Deletes user by id
     * @param userId
     */
    @Transactional
    @Override
    public void deleteUser(Long userId) {
        if(! userRepository.existsById(userId)) {
            throw new HelperAppException(HttpStatus.NOT_FOUND, HelperAppConstants.HA001);
        }
        try {
            userRepository.deleteById(userId);
        } catch (Exception e) {
            throw new HelperAppException(HttpStatus.INTERNAL_SERVER_ERROR, HelperAppConstants.HA006);
        }
    }
}
