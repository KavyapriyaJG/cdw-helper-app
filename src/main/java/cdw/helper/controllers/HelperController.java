package cdw.helper.controllers;

import cdw.helper.dto.SuccessResponseDTO;
import cdw.helper.entities.Appointment;
import cdw.helper.repositories.AppointmentRepository;
import cdw.helper.services.helperservices.HelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        successResponseDTO.setStatus(HttpStatus.OK);
        successResponseDTO.setBody(appointments);

        return ResponseEntity.ok().body(successResponseDTO);
    }
}
