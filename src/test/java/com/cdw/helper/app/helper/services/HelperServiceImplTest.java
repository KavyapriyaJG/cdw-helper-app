package com.cdw.helper.app.helper.services;

import com.cdw.helper.app.common.entities.Appointment;
import com.cdw.helper.app.common.repositories.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Suite that holds testcases for Helper Service Operations
 */
public class HelperServiceImplTest {

    private HelperServiceImpl helperService;
    private AppointmentRepository appointmentRepository;

    @BeforeEach
    void setUp() {
        appointmentRepository = mock(AppointmentRepository.class);
        helperService = new HelperServiceImpl(appointmentRepository);
    }

    @Test
    void fetchAppointments_ShouldReturnAppointments_WhenHelperHasAppointments() {
        Long helperId = 1L;
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(new Appointment());
        appointments.add(new Appointment());

        when(appointmentRepository.findByHelperId(helperId)).thenReturn(appointments);

        List<Appointment> result = helperService.fetchAppointments(helperId);

        assertEquals(2, result.size());
        verify(appointmentRepository, times(1)).findByHelperId(helperId);
    }

    @Test
    void fetchAppointments_ShouldReturnEmptyList_WhenHelperHasNoAppointments() {
        Long helperId = 1L;

        when(appointmentRepository.findByHelperId(helperId)).thenReturn(new ArrayList<>());

        List<Appointment> result = helperService.fetchAppointments(helperId);

        assertTrue(result.isEmpty());
        verify(appointmentRepository, times(1)).findByHelperId(helperId);
    }
}
