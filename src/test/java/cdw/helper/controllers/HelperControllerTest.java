package cdw.helper.controllers;

import cdw.helper.dto.SuccessResponseDTO;
import cdw.helper.entities.Appointment;
import cdw.helper.services.helperservices.HelperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Suite that holds test cases for Helper Controller Operations
 */
public class HelperControllerTest {

    @Mock
    private HelperService helperService;

    @InjectMocks
    private HelperController helperController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void fetchAppointments_ShouldReturnAppointmentsForHelper() {
        Long helperId = 1L;
        Appointment appointment1 = new Appointment();
        appointment1.setId(1L);
        Appointment appointment2 = new Appointment();
        appointment2.setId(2L);
        List<Appointment> appointments = Arrays.asList(appointment1, appointment2);

        when(helperService.fetchAppointments(helperId)).thenReturn(appointments);
        ResponseEntity<SuccessResponseDTO> response = helperController.fetchAppointments(helperId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(appointments, response.getBody().getBody());
    }
}

