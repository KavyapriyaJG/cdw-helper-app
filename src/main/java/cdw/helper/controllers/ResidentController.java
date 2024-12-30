package cdw.helper.controllers;

import cdw.helper.dto.AppointmentDTO;
import cdw.helper.dto.AvailableHelpersDTO;
import cdw.helper.dto.SuccessResponseDTO;
import cdw.helper.services.residentservices.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        successResponseDTO.setStatus(HttpStatus.CREATED);
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
        successResponseDTO.setStatus(HttpStatus.OK);
        successResponseDTO.setBody(users);

        return ResponseEntity.ok().body(successResponseDTO);
    }
}
