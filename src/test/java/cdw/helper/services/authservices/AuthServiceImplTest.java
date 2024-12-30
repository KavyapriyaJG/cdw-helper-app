package cdw.helper.services.authservices;

import cdw.helper.constants.HelperAppConstants;
import cdw.helper.dto.UserRegistrationDTO;
import cdw.helper.entities.Role;
import cdw.helper.entities.User;
import cdw.helper.exceptions.HelperAppException;
import cdw.helper.mappers.UserMapper;
import cdw.helper.repositories.HelperDetailsRepository;
import cdw.helper.repositories.RoleRepository;
import cdw.helper.repositories.UserRepository;
import cdw.helper.security.JwtFilter;
import cdw.helper.utilities.JwtUtility;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Suite that holds test cases for Authentication Service operations
 */
public class AuthServiceImplTest {

    private AuthServiceImpl authService;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private HelperDetailsRepository helperDetailsRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;
    private JwtUtility jwtUtility;
    private JwtFilter jwtFilter;

    private Role adminRole;
    private Role residentRole;
    private Role helperRole;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        helperDetailsRepository = mock(HelperDetailsRepository.class);
        userMapper = mock(UserMapper.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtUtility = mock(JwtUtility.class);
        jwtFilter = mock(JwtFilter.class);

        authService = new AuthServiceImpl(userRepository, roleRepository, helperDetailsRepository, userMapper, passwordEncoder, jwtUtility, jwtFilter);

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

        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(adminRole);
        when(roleRepository.findByName("ROLE_HELPER")).thenReturn(helperRole);
        when(roleRepository.findByName("ROLE_RESIDENT")).thenReturn(residentRole);

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

        when(passwordEncoder.matches(userPassword, encodedPassword)).thenReturn(true);
        when(jwtUtility.generateToken(anyString(), anyString())).thenReturn("token123");

        String adminToken = authService.login(adminEmail, userPassword);
        String helperToken = authService.login(helperEmail, userPassword);
        String residentToken = authService.login(residentEmail, userPassword);

        assertEquals("token123", adminToken);
        assertEquals("token123", helperToken);
        assertEquals("token123", residentToken);

        verify(userRepository, times(3)).findByEmailAddress(anyString());
        verify(passwordEncoder, times(3)).matches(eq(userPassword), eq(encodedPassword));
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
        when(passwordEncoder.matches(invalidPassword, "encodedPassword")).thenReturn(false);

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
        when(passwordEncoder.matches(invalidPassword, "encodedPassword")).thenReturn(true);

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
