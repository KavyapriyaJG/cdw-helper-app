package com.cdw.helper.app.auth.services;

import com.cdw.helper.app.common.constants.HelperAppConstants;
import com.cdw.helper.app.common.dto.UserRegistrationDTO;
import com.cdw.helper.app.common.entities.Role;
import com.cdw.helper.app.common.entities.User;
import com.cdw.helper.app.common.exceptions.HelperAppException;
import com.cdw.helper.app.common.mappers.UserMapper;
import com.cdw.helper.app.common.repositories.RoleRepository;
import com.cdw.helper.app.common.repositories.UserRepository;
import com.cdw.helper.app.common.security.JwtFilter;
import com.cdw.helper.app.common.utilities.JwtUtility;
import com.cdw.helper.app.helper.repositories.HelperDetailsRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Suite that holds test cases for Authentication Service operations
 */
public class AuthServiceImplTest {

    private AuthServiceImpl authService;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private HelperDetailsRepository helperDetailsRepository;
    private UserMapper userMapper;
    private JwtUtility jwtUtility;
    private JwtFilter jwtFilter;
    private AuthenticationManager authenticationManager;

    private Role adminRole;
    private Role residentRole;
    private Role helperRole;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        helperDetailsRepository = mock(HelperDetailsRepository.class);
        userMapper = mock(UserMapper.class);
        jwtUtility = mock(JwtUtility.class);
        jwtFilter = mock(JwtFilter.class);
        authenticationManager = mock(AuthenticationManager.class);

        MockitoAnnotations.openMocks(this);
        authService = new AuthServiceImpl(userRepository, roleRepository, helperDetailsRepository, userMapper, jwtUtility, jwtFilter, authenticationManager);

        // Setting up 3 different roles
        adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");

        helperRole = new Role();
        helperRole.setName("ROLE_HELPER");

        residentRole = new Role();
        residentRole.setName("ROLE_RESIDENT");
    }

    @Test
    void register_ShouldSaveAdminHelperResidentUsers_WhenValidInput() {
        // Setting mock data for the 3 types of users
        UserRegistrationDTO adminDTO = new UserRegistrationDTO();
        UserRegistrationDTO helperDTO = new UserRegistrationDTO();
        UserRegistrationDTO residentDTO = new UserRegistrationDTO();

        adminDTO.setEmailAddress("admin@mail.com");
        adminDTO.setPassword("password");
        adminDTO.setFirstName("Kavya");
        adminDTO.setLastName("priya");
        adminDTO.setDob("2025-01-01");
        adminDTO.setGender("Female");

        helperDTO.setEmailAddress("helper@mail.com");
        helperDTO.setPassword("password");
        helperDTO.setFirstName("Kavya");
        helperDTO.setLastName("priya");
        helperDTO.setDob("2025-01-01");
        helperDTO.setGender("Female");
        helperDTO.setSpecialization("Electrician");
        helperDTO.setRating(4.5f);
        helperDTO.setHourlyRate(100.0f);

        residentDTO.setEmailAddress("resident@mail.com");
        residentDTO.setPassword("password");
        residentDTO.setFirstName("Kavya");
        residentDTO.setLastName("priya");
        residentDTO.setDob("2025-01-01");
        residentDTO.setGender("Female");
        residentDTO.setApartmentNumber("A-101");

        User admin = new User();
        User helper = new User();
        User resident = new User();

        // Mocks for the different roles
        when(userMapper.mapUserRegistrationDTOToUser(adminDTO, "ADMIN")).thenReturn(admin);
        when(userMapper.mapUserRegistrationDTOToUser(helperDTO, "HELPER")).thenReturn(helper);
        when(userMapper.mapUserRegistrationDTOToUser(residentDTO, "RESIDENT")).thenReturn(resident);

        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.ofNullable(adminRole));
        when(roleRepository.findByName("ROLE_HELPER")).thenReturn(Optional.ofNullable(helperRole));
        when(roleRepository.findByName("ROLE_RESIDENT")).thenReturn(Optional.ofNullable(residentRole));

        authService.register(adminDTO, "ADMIN");
        authService.register(helperDTO, "HELPER");
        authService.register(residentDTO, "RESIDENT");

        verify(userRepository, times(1)).save(admin);
        verify(userRepository, times(1)).save(helper);
        verify(userRepository, times(1)).save(resident);
    }

    @Test
    void login_ShouldReturnToken_WhenAdminHelperResidentLoginSuccessfully() {
        String adminEmail = "admin@example.com";
        String helperEmail = "helper@example.com";
        String residentEmail = "resident@example.com";

        String userPassword = "userPassword";
        String encodedPassword = "encodedPassword";

        User admin = new User();
        admin.setEmailAddress(adminEmail);
        admin.setPassword(encodedPassword);
        admin.setApprovedByAdmin(true);
        admin.setRole(adminRole);

        User helper = new User();
        helper.setEmailAddress(helperEmail);
        helper.setPassword(encodedPassword);
        helper.setApprovedByAdmin(true);
        helper.setRole(helperRole);

        User resident = new User();
        resident.setEmailAddress(residentEmail);
        resident.setPassword(encodedPassword);
        resident.setApprovedByAdmin(true);
        resident.setRole(residentRole);

        when(userRepository.findByEmailAddress(adminEmail)).thenReturn(Optional.of(admin));
        when(userRepository.findByEmailAddress(helperEmail)).thenReturn(Optional.of(helper));
        when(userRepository.findByEmailAddress(residentEmail)).thenReturn(Optional.of(resident));

        when(jwtUtility.generateToken(anyString(), anyString())).thenReturn("token123");

        String adminToken = authService.login(adminEmail, userPassword);
        String helperToken = authService.login(helperEmail, userPassword);
        String residentToken = authService.login(residentEmail, userPassword);

        assertEquals("token123", adminToken);
        assertEquals("token123", helperToken);
        assertEquals("token123", residentToken);

        verify(userRepository, times(3)).findByEmailAddress(anyString());
        verify(jwtUtility, times(3)).generateToken(anyString(), anyString());
    }

    @Test
    void register_ShouldThrowException_WhenRoleIsInvalid() {
        UserRegistrationDTO invalidDTO = new UserRegistrationDTO();
        invalidDTO.setEmailAddress("invalid@mail.com");
        invalidDTO.setPassword("password");

        when(userMapper.mapUserRegistrationDTOToUser(invalidDTO, "INVALID_ROLE"))
                .thenThrow(new HelperAppException(HttpStatus.CONFLICT, HelperAppConstants.HA003));

        HelperAppException exception = assertThrows(HelperAppException.class, () -> authService.register(invalidDTO, "INVALID_ROLE"));

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals(HelperAppConstants.HA003, exception.getMessage());
    }

    @Test
    void login_ShouldThrowException_WhenPasswordIsInvalid() {
        String email = "user@example.com";
        String invalidPassword = "wrongPassword";

        User user = new User();
        user.setPassword("encodedPassword");
        user.setApprovedByAdmin(true);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(user));

        HelperAppException exception = assertThrows(HelperAppException.class, () -> authService.login(email, invalidPassword));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        assertEquals(HelperAppConstants.HA003, exception.getMessage());
    }


    @Test
    void login_ShouldThrowException_WhenUserNotApprovedByAdmin() {
        String email = "user@example.com";
        String invalidPassword = "password";

        User user = new User();
        user.setPassword("encodedPassword");
        user.setApprovedByAdmin(false);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(user));

        HelperAppException exception = assertThrows(HelperAppException.class, () -> authService.login(email, invalidPassword));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        assertEquals(HelperAppConstants.HA003, exception.getMessage());
    }

    @Test
    void logout_ShouldInvalidateToken_ForAdminHelperResident() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String token = "validToken";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        authService.logout(request);

        verify(jwtFilter, times(1)).invalidateToken(token);
    }
}
