package com.cdw.helper.app.admin.controllers;

import com.cdw.helper.app.admin.services.AdminService;
import com.cdw.helper.app.common.dto.SuccessResponseDTO;
import com.cdw.helper.app.common.dto.UserUpdationDTO;
import com.cdw.helper.app.common.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Suite that holds test cases for Admin Controller Operations
 */
public class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchAllUsers() {
        List<User> users = new ArrayList<>();
        User user1 = new User();
        User user2 = new User();
        users.add(user1);
        users.add(user2);

        when(adminService.fetchAllUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = adminController.fetchAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(adminService, times(1)).fetchAllUsers();
    }

    @Test
    void testApproveUser() {
        Long userId = 12345L;
        String status = "Admin approved successfully!";

        when(adminService.verifyUser(userId)).thenReturn(status);

        ResponseEntity<SuccessResponseDTO> response = adminController.approveUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(status, response.getBody().getBody());
        verify(adminService, times(1)).verifyUser(userId);
    }

    @Test
    void testUpdateUser() {
        Long userId = 12345L;
        UserUpdationDTO userUpdationDTO = new UserUpdationDTO();
        userUpdationDTO.setFirstName("Kavya");
        userUpdationDTO.setLastName("priya");
        userUpdationDTO.setDob("2025-01-01");
        userUpdationDTO.setGender("Female");
        userUpdationDTO.setSpecialization("Electrician");
        userUpdationDTO.setRating(4.5f);
        userUpdationDTO.setHourlyRate(100.0f);

        doNothing().when(adminService).updateUser(userId, userUpdationDTO);

        ResponseEntity<SuccessResponseDTO> response = adminController.updateUser(userId, userUpdationDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated successfully !", response.getBody().getBody());
        verify(adminService, times(1)).updateUser(userId, userUpdationDTO);
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;

        doNothing().when(adminService).deleteUser(userId);

        ResponseEntity<Object> response = adminController.deleteUser(userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(adminService, times(1)).deleteUser(userId);
    }
}
