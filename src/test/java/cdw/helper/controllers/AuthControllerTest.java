package cdw.helper.controllers;

import cdw.helper.dto.SuccessResponseDTO;
import cdw.helper.dto.UserLoginDTO;
import cdw.helper.dto.UserRegistrationDTO;
import cdw.helper.services.authservices.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Suite that holds test cases for Authentication Controller Operations
 */
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setEmailAddress("kavya@mail.com");
        userRegistrationDTO.setPassword("password");
        userRegistrationDTO.setFirstName("Kavya");
        userRegistrationDTO.setLastName("priya");
        userRegistrationDTO.setDob("2025-01-01");
        userRegistrationDTO.setGender("Female");
        userRegistrationDTO.setSpecialization("Electrician");
        userRegistrationDTO.setRating(4.5f);
        userRegistrationDTO.setHourlyRate(100.0f);

        String role = "helper";

        doNothing().when(authService).register(userRegistrationDTO, role);

        ResponseEntity<SuccessResponseDTO> response = authController.register(userRegistrationDTO, role);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(role + " created successfully !", response.getBody().getBody());
        verify(authService, times(1)).register(userRegistrationDTO, role);
    }

    @Test
    void testLogin() {
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setEmailAddress("kavya@mail.com");
        userLoginDTO.setPassword("password");

        String token = "sampleToken";
        when(authService.login(userLoginDTO.getEmailAddress(), userLoginDTO.getPassword())).thenReturn(token);

        ResponseEntity<SuccessResponseDTO> response = authController.login(userLoginDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(token, response.getBody().getBody());
        verify(authService, times(1)).login(userLoginDTO.getEmailAddress(), userLoginDTO.getPassword());
    }

    @Test
    void testLogout() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        doNothing().when(authService).logout(request);

        ResponseEntity<SuccessResponseDTO> response = authController.logout(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User Logged out successfully !", response.getBody().getBody());
        verify(authService, times(1)).logout(request);
    }

}
