package cdw.helper.services.helperservices;

import cdw.helper.entities.Appointment;
import cdw.helper.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Class that holds Helper Service Operations
 */
@Service
public class HelperServiceImpl implements HelperService {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public HelperServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Fetch Appointments of a helper
     * @param helperId
     * @return
     */
    @Override
    public List<Appointment> fetchAppointments(Long helperId) {
        List<Appointment> appointments = appointmentRepository.findByHelperId(helperId);
        return appointments;
    }
}
