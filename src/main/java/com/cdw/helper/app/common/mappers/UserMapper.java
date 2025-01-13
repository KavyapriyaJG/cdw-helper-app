package com.cdw.helper.app.common.mappers;

import com.cdw.helper.app.common.dto.UserRegistrationDTO;
import com.cdw.helper.app.common.dto.UserUpdationDTO;
import com.cdw.helper.app.common.entities.RoleEnum;
import com.cdw.helper.app.common.entities.User;
import com.cdw.helper.app.common.entities.UserDetails;
import com.cdw.helper.app.common.utilities.CommonUtility;
import com.cdw.helper.app.common.utilities.DateUtility;
import com.cdw.helper.app.helper.entities.HelperDetails;
import com.cdw.helper.app.resident.entities.ResidentDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * User Mapper class holds useful mapper methods for User entity
 */
@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Maps userRegistrationDTO on to the User entity
     * @param userRegistrationDTO
     * @param role
     * @return
     */
    public User mapUserRegistrationDTOToUser(UserRegistrationDTO userRegistrationDTO, String role) {

        User user = new User();
        UserDetails userDetails = new UserDetails();

        userDetails.setFirstName(userRegistrationDTO.getFirstName());
        userDetails.setLastName(userRegistrationDTO.getLastName());

        userDetails.setDob(DateUtility.convertStringToLocalDate(userRegistrationDTO.getDob()));
        userDetails.setGender(userRegistrationDTO.getGender());

        userDetails.setCreatedAt(LocalDateTime.now());
        userDetails.setUpdatedAt(LocalDateTime.now());

        user.setUserDetails(userDetails);
        user.setEmailAddress(userRegistrationDTO.getEmailAddress());
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));

        role = CommonUtility.roleStringProcess(role).toUpperCase();
        // Set this to be false for Residents and Helpers/ Technicians; true for Admin
        user.setApprovedByAdmin(role == RoleEnum.ROLE_ADMIN.toString());

        // Helper Details
        if(role.equals(RoleEnum.ROLE_HELPER.toString())) {
            HelperDetails helperDetails = new HelperDetails();
            helperDetails.setSpecialization(userRegistrationDTO.getSpecialization());
            helperDetails.setRating(userRegistrationDTO.getRating());
            helperDetails.setHourlyRate(userRegistrationDTO.getHourlyRate());

            userDetails.setHelperDetails(helperDetails);
        }

        // Resident Details
        if(role.equals(RoleEnum.ROLE_RESIDENT.toString())) {
            ResidentDetails residentDetails = new ResidentDetails();
            residentDetails.setApartmentNumber(userRegistrationDTO.getApartmentNumber());

            userDetails.setResidentDetails(residentDetails);
        }

        return user;
    }

    /**
     * Maps UserUpdationDTO on to the User entity
     * @param userUpdationDTO
     * @param user
     * @return
     */
    public User mapUpdatedUserToUserDTO(UserUpdationDTO userUpdationDTO, User user) {
        UserDetails userDetails = user.getUserDetails();

        userDetails.setFirstName(userUpdationDTO.getFirstName() == null ? user.getUserDetails().getFirstName() : userUpdationDTO.getFirstName());
        userDetails.setLastName(userUpdationDTO.getLastName() == null ?  user.getUserDetails().getLastName() : userUpdationDTO.getLastName());

        userDetails.setDob(userUpdationDTO.getDob() == null ? user.getUserDetails().getDob() : DateUtility.convertStringToLocalDate(userUpdationDTO.getDob()));

        userDetails.setGender(userUpdationDTO.getGender() == null ? user.getUserDetails().getGender() : userUpdationDTO.getGender());

        user.setUserDetails(userDetails);
        userDetails.setUpdatedAt(LocalDateTime.now());

        // Helper Details
        if(user.getRole().getName().equals(RoleEnum.ROLE_HELPER.toString())) {
            HelperDetails helperDetails = userDetails.getHelperDetails();
            helperDetails.setSpecialization(userUpdationDTO.getSpecialization() == null ? helperDetails.getSpecialization() : userUpdationDTO.getSpecialization());
            helperDetails.setHourlyRate(userUpdationDTO.getHourlyRate() == 0.0 ? helperDetails.getHourlyRate() : userUpdationDTO.getHourlyRate());
            helperDetails.setRating(userUpdationDTO.getRating() == 0.0 ? helperDetails.getRating() : userUpdationDTO.getRating());

            userDetails.setHelperDetails(helperDetails);
        }

        // Resident Details
        if(user.getRole().getName().equals(RoleEnum.ROLE_RESIDENT.toString())) {
            ResidentDetails residentDetails = userDetails.getResidentDetails();
            residentDetails.setApartmentNumber(userUpdationDTO.getApartmentNumber() == null ? residentDetails.getApartmentNumber() : userUpdationDTO.getApartmentNumber());
        }

        return user;
    }
}
