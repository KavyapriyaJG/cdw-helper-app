package com.cdw.helper.app.auth.services;

import com.cdw.helper.app.common.constants.HelperAppConstants;
import com.cdw.helper.app.common.dto.UserRegistrationDTO;
import com.cdw.helper.app.common.entities.Role;
import com.cdw.helper.app.common.entities.User;
import com.cdw.helper.app.common.exceptions.HelperAppException;
import com.cdw.helper.app.common.exceptions.ResourceNotFoundException;
import com.cdw.helper.app.common.mappers.UserMapper;
import com.cdw.helper.app.common.repositories.RoleRepository;
import com.cdw.helper.app.common.repositories.UserRepository;
import com.cdw.helper.app.common.security.JwtFilter;
import com.cdw.helper.app.common.utilities.CommonUtility;
import com.cdw.helper.app.common.utilities.JwtUtility;
import com.cdw.helper.app.helper.repositories.HelperDetailsRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Holds useful for Authentication Service operations
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final HelperDetailsRepository helperDetailsRepository;
    private final UserMapper userMapper;
    private final JwtUtility jwtUtility;

    private final JwtFilter jwtFilter;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, HelperDetailsRepository helperDetailsRepository, UserMapper userMapper, JwtUtility jwtUtility, JwtFilter jwtFilter, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.helperDetailsRepository = helperDetailsRepository;
        this.userMapper = userMapper;
        this.jwtUtility = jwtUtility;
        this.jwtFilter = jwtFilter;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registers a user
     * @param userRegistrationDTO
     * @param userRole
     */
    @Transactional
    @Override
    public void register(UserRegistrationDTO userRegistrationDTO, String userRole) {
        try {
            User user = userMapper.mapUserRegistrationDTOToUser(userRegistrationDTO, userRole);

            Optional<Role> role = roleRepository.findByName(CommonUtility.roleStringProcess(userRole));
            user.setRole(role.get());

            userRepository.save(user);
        } catch (Exception e) {
            throw new HelperAppException(HttpStatus.CONFLICT,HelperAppConstants.HA003);
        }
    }

    /**
     * Logs in user
     * @param email
     * @param password
     * @return
     */
    public String login(String email, String password) {
        try {
            authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(email, password));

            User user = userRepository.findByEmailAddress(email)
                    .orElseThrow(() -> new ResourceNotFoundException(HelperAppConstants.HA001));

            // Access Denial for non-approved users
            if (!user.isApprovedByAdmin()) {
                throw new HelperAppException(HttpStatus.FORBIDDEN, HelperAppConstants.HA005);
            }

            return jwtUtility.generateToken(user.getEmailAddress(), user.getRole().toString());
        } catch (Exception e) {
            throw new HelperAppException(HttpStatus.UNAUTHORIZED,HelperAppConstants.HA003);
        }
    }

    /**
     * Logs a user out
     * @param request
     */
    @Override
    public void logout(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            jwtFilter.invalidateToken(token);
        }
    }
}
