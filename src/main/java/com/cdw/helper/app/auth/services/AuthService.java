package com.cdw.helper.app.auth.services;

import com.cdw.helper.app.common.dto.UserRegistrationDTO;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Interface for Authentication service operations
 */
public interface AuthService {

    void register(UserRegistrationDTO userRegistrationDTO, String role);

    String login(String email, String password);

    void logout(HttpServletRequest request);
}
