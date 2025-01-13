package com.cdw.helper.app.resident.controllers;

import com.cdw.helper.app.common.dto.AppointmentDTO;
import com.cdw.helper.app.common.dto.AvailableHelpersDTO;
import com.cdw.helper.app.common.dto.SuccessResponseDTO;
import com.cdw.helper.app.resident.services.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Class that holds Resident Controller Operations
 */
@RestController
@RequestMapping("/resident")
public class ResidentController {

    private final ResidentService residentService;

    @Autowired
    public ResidentController(ResidentService residentService) {
        this.residentService = residentService;
    }

    /**
     * Book an appointment for the resident with the requested helper
     * @param residentId
     * @param appointmentDTO
     * @return
     */
    @PostMapping("/{id}/book-appointment")
    public ResponseEntity<SuccessResponseDTO> bookAppointment(@PathVariable("id") Long residentId, @RequestBody AppointmentDTO appointmentDTO) {
        residentService.bookAppointment(residentId, appointmentDTO);

        SuccessResponseDTO successResponseDTO = new SuccessResponseDTO();
        successResponseDTO.setSuccess(true);
        successResponseDTO.setBody("Appointment booked successfully !");

        return ResponseEntity.ok().body(successResponseDTO);
    }

    /**
     * Fetches all available helpers/technicians optionally filtered by skill and date
     * @param skill
     * @param dateTime
     * @return
     */
    @GetMapping("/helper")
    public ResponseEntity<SuccessResponseDTO> fetchHelpers(@RequestParam(value = "skill", required = false) String skill, @RequestParam(value = "date", required = false) String dateTime) {
        List<AvailableHelpersDTO> users = residentService.fetchAllAvailableHelpers(skill, dateTime);

        SuccessResponseDTO successResponseDTO = new SuccessResponseDTO();
        successResponseDTO.setSuccess(true);
        successResponseDTO.setBody(users);

        return ResponseEntity.ok().body(successResponseDTO);
    }
}
