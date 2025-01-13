package com.cdw.helper.app.common.security;

import com.cdw.helper.app.common.constants.HelperAppConstants;
import com.cdw.helper.app.common.entities.User;
import com.cdw.helper.app.common.exceptions.HelperAppException;
import com.cdw.helper.app.common.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


/**
 * Class that extends the UserDetailsService class
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads a user by the specified id (email address)
     * @param email
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new HelperAppException(HttpStatus.NOT_FOUND, HelperAppConstants.HA001));

        // Return UserDetails object with user's email, password, and roles/authorities
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmailAddress())
                .password(user.getPassword())
                // Role-based authority
                .authorities(new ArrayList<GrantedAuthority>() {{
                    add(new SimpleGrantedAuthority(user.getRole().getName())); // Convert enum to String
                }})
                .build();
    }
}
