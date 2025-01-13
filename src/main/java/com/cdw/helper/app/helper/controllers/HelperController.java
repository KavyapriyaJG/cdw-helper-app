package com.cdw.helper.app.helper.controllers;

import com.cdw.helper.app.common.dto.SuccessResponseDTO;
import com.cdw.helper.app.common.entities.Appointment;
import com.cdw.helper.app.common.repositories.AppointmentRepository;
import com.cdw.helper.app.helper.services.HelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Class that holds Helper Controller Operations
 */
@RestController
@RequestMapping("/helper")
public class HelperController {

    private final AppointmentRepository appointmentRepository;
    private final HelperService helperService;

    @Autowired
    public HelperController(AppointmentRepository appointmentRepository, HelperService helperService) {
        this.appointmentRepository = appointmentRepository;
        this.helperService = helperService;
    }

    /**
     * Fetch all appointments of a helper
     * @return
     */
    @GetMapping("/{id}/appointments")
    public ResponseEntity<SuccessResponseDTO> fetchAppointments(@PathVariable("id") Long helperId) {
        List<Appointment> appointments = helperService.fetchAppointments(helperId);

        SuccessResponseDTO successResponseDTO = new SuccessResponseDTO();
        successResponseDTO.setSuccess(true);
        successResponseDTO.setBody(appointments);

        return ResponseEntity.ok().body(successResponseDTO);
    }
}
