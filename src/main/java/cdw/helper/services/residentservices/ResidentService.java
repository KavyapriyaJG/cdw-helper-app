package cdw.helper.services.residentservices;

import cdw.helper.dto.AppointmentDTO;
import cdw.helper.dto.AvailableHelpersDTO;

import java.util.List;

/**
 * Interface for Resident Service Operations
 */
public interface ResidentService {
    void bookAppointment(Long residentId, AppointmentDTO appointmentDTO);
    List<AvailableHelpersDTO> fetchAllAvailableHelpers(String skill, String dateTime);
}
