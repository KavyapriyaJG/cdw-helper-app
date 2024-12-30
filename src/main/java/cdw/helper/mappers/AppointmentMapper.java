package cdw.helper.mappers;

import cdw.helper.dto.AppointmentDTO;
import cdw.helper.entities.Appointment;
import cdw.helper.utilities.DateUtility;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Appointment Mapper class holds useful mapper methods for Appointment entity
 */
@Component
public class AppointmentMapper {

    /**
     * Maps AppointmentDTO on to the Appointment entity
     * @param residentId
     * @param appointmentDTO
     * @return
     */
    public Appointment mapAppointmentDTOtoAppointment(Long residentId, AppointmentDTO appointmentDTO) {
        Appointment appointment = new Appointment();

        LocalDateTime dateTime = DateUtility.convertStringToLocalDateTime(appointmentDTO.getScheduledAt());

        appointment.setScheduledAt(dateTime);
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setUpdatedAt(LocalDateTime.now());

        return appointment;
    }
}
