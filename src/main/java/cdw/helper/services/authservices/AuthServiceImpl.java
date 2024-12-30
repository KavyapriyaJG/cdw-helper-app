package cdw.helper.services.authservices;

import cdw.helper.constants.HelperAppConstants;
import cdw.helper.dto.UserRegistrationDTO;
import cdw.helper.entities.Role;
import cdw.helper.entities.User;
import cdw.helper.exceptions.HelperAppException;
import cdw.helper.exceptions.ResourceNotFoundException;
import cdw.helper.mappers.UserMapper;
import cdw.helper.repositories.HelperDetailsRepository;
import cdw.helper.repositories.RoleRepository;
import cdw.helper.repositories.UserRepository;
import cdw.helper.security.JwtFilter;
import cdw.helper.utilities.CommonUtility;
import cdw.helper.utilities.JwtUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Holds useful for Authentication Service operations
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final HelperDetailsRepository helperDetailsRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtility jwtUtility;

    private final JwtFilter jwtFilter;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, HelperDetailsRepository helperDetailsRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, JwtUtility jwtUtility, JwtFilter jwtFilter) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.helperDetailsRepository = helperDetailsRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtility = jwtUtility;
        this.jwtFilter = jwtFilter;
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

            Role role = roleRepository.findByName(CommonUtility.roleStringProcess(userRole));
            user.setRole(role);

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
            User user = userRepository.findByEmailAddress(email)
                    .orElseThrow(() -> new ResourceNotFoundException(HelperAppConstants.HA001));

            // Password check
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new HelperAppException(HttpStatus.UNAUTHORIZED, HelperAppConstants.HA004);
            }

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
