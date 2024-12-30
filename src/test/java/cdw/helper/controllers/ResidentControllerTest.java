package cdw.helper.controllers;

import cdw.helper.dto.AppointmentDTO;
import cdw.helper.dto.AvailableHelpersDTO;
import cdw.helper.dto.SuccessResponseDTO;
import cdw.helper.entities.*;
import cdw.helper.services.residentservices.ResidentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Suite that holds test cases for Resident Controller Operations
 */
public class ResidentControllerTest {

    @Mock
    private ResidentService residentService;

    @InjectMocks
    private ResidentController residentController;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user1 = new User();
        user1.setId(1L);
        user1.setEmailAddress("kavya@mail.com");
        user1.setPassword("password123");
        user1.setRole(new Role());
        user1.getRole().setId(1L);
        user1.getRole().setName("ROLE_HELPER");
        user1.setApprovedByAdmin(true);

        UserDetails userDetails1 = new UserDetails();
        userDetails1.setFirstName("Kavya");
        userDetails1.setLastName("K");
        userDetails1.setDob(LocalDate.of(2002, 11, 11));
        userDetails1.setGender("Female");

        HelperDetails helperDetails1 = new HelperDetails();
        helperDetails1.setSpecialization("Plumber");
        helperDetails1.setRating(4.5f);
        helperDetails1.setHourlyRate(20.0f);
        userDetails1.setHelperDetails(helperDetails1);
        user1.setUserDetails(userDetails1);

        user2 = new User();
        user2.setId(2L);
        user2.setEmailAddress("priya@mail.com");
        user2.setPassword("password456");
        user2.setRole(new Role());
        user1.getRole().setId(2L);
        user1.getRole().setName("ROLE_HELPER");
        user2.setApprovedByAdmin(true);

        UserDetails userDetails2 = new UserDetails();
        userDetails2.setFirstName("Priya");
        userDetails2.setLastName("P");
        userDetails2.setDob(LocalDate.of(2011, 11, 11));
        userDetails2.setGender("Female");

        HelperDetails helperDetails2 = new HelperDetails();
        helperDetails2.setSpecialization("Electrician");
        helperDetails2.setRating(4.8f);
        helperDetails2.setHourlyRate(25.0f);
        userDetails2.setHelperDetails(helperDetails2);
        user2.setUserDetails(userDetails2);
    }

    @Test
    void testBookAppointment() {
        Long residentId = 3L;
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setScheduledAt("2025-01-01T10:00");
        appointmentDTO.setHelperId(2L);

        doNothing().when(residentService).bookAppointment(residentId, appointmentDTO);

        ResponseEntity<SuccessResponseDTO> response = residentController.bookAppointment(residentId, appointmentDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Appointment booked successfully !", response.getBody().getBody());
        verify(residentService, times(1)).bookAppointment(residentId, appointmentDTO);
    }

    @Test
    void testFetchHelpersWithoutFilters() {
        List<AvailableHelpersDTO> mockUsers = Arrays.asList(new AvailableHelpersDTO(), new AvailableHelpersDTO());

        when(residentService.fetchAllAvailableHelpers(null, null)).thenReturn(mockUsers);

        ResponseEntity<SuccessResponseDTO> response = residentController.fetchHelpers(null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUsers, response.getBody().getBody());
        verify(residentService, times(1)).fetchAllAvailableHelpers(null, null);
    }

    @Test
    void testFetchHelpersWithFilters() {
        String skill = "Plumber";
        String date = "2025-12-12";

        List<AvailableHelpersDTO> availableHelpers = Arrays.asList(new AvailableHelpersDTO());

        when(residentService.fetchAllAvailableHelpers(skill, date)).thenReturn(availableHelpers);

        ResponseEntity<SuccessResponseDTO> response = residentController.fetchHelpers(skill, date);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(availableHelpers, response.getBody().getBody());
        verify(residentService, times(1)).fetchAllAvailableHelpers(skill, date);
    }
}
