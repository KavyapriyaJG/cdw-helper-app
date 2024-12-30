package cdw.helper.controllers;

import cdw.helper.dto.SuccessResponseDTO;
import cdw.helper.dto.UserLoginDTO;
import cdw.helper.dto.UserRegistrationDTO;
import cdw.helper.services.authservices.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * Class that holds the Authentication Controller operations
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Registers a user to the system
     * @param userRegistrationDTO
     * @return
     */
    @PostMapping("/register/{role}")
    public ResponseEntity<SuccessResponseDTO> register(@RequestBody UserRegistrationDTO userRegistrationDTO, @PathVariable(value = "role") String role) {
        authService.register(userRegistrationDTO, role);

        SuccessResponseDTO successResponseDTO = new SuccessResponseDTO();
        successResponseDTO.setStatus(HttpStatus.CREATED);
        successResponseDTO.setBody(role + " created successfully !");

        return ResponseEntity.created(URI.create("/register")).body(successResponseDTO);
    }

    /**
     * Logs in a user
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<SuccessResponseDTO> login(@RequestBody UserLoginDTO userLoginDTO) {
        String token = authService.login(userLoginDTO.getEmailAddress(), userLoginDTO.getPassword());
        SuccessResponseDTO successResponseDTO = new SuccessResponseDTO();
        successResponseDTO.setStatus(HttpStatus.OK);
        successResponseDTO.setBody(token);
        return ResponseEntity.ok().body(successResponseDTO);
    }

    /**
     * Logs out a user
     * @param request
     * @return
     */
    @GetMapping("/logout")
    public ResponseEntity<SuccessResponseDTO> logout(HttpServletRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            authService.logout(request);
        }

        SuccessResponseDTO successResponseDTO = new SuccessResponseDTO();
        successResponseDTO.setStatus(HttpStatus.OK);
        successResponseDTO.setBody("User Logged out successfully !");

        return ResponseEntity.ok().body(successResponseDTO);
    }
}
