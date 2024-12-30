package cdw.helper.services.helperservices;

import cdw.helper.entities.Appointment;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Interface for Helper Service Operations
 */
public interface HelperService {
    List<Appointment> fetchAppointments(Long helperId);
}
