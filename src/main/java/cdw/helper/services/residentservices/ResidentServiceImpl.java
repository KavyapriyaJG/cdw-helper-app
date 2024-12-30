package cdw.helper.services.residentservices;

import cdw.helper.constants.HelperAppConstants;
import cdw.helper.dto.AppointmentDTO;
import cdw.helper.dto.AvailableHelpersDTO;
import cdw.helper.entities.Appointment;
import cdw.helper.entities.User;
import cdw.helper.exceptions.HelperAppException;
import cdw.helper.exceptions.ResourceNotFoundException;
import cdw.helper.mappers.AppointmentMapper;
import cdw.helper.repositories.AppointmentRepository;
import cdw.helper.repositories.HelperDetailsRepository;
import cdw.helper.repositories.UserRepository;
import cdw.helper.utilities.DateUtility;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Class that holds Resident Service Operations
 */
@Service
public class ResidentServiceImpl implements ResidentService {

    private final UserRepository userRepository;
    private final HelperDetailsRepository helperDetailsRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;


    public ResidentServiceImpl(UserRepository userRepository, HelperDetailsRepository helperDetailsRepository, AppointmentRepository appointmentRepository, AppointmentMapper appointmentMapper) {
        this.userRepository = userRepository;
        this.helperDetailsRepository = helperDetailsRepository;
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
    }

    /**
     * Book an appointment for the resident with the requested helper
     * @param residentId
     * @param appointmentDTO
     */
    @Transactional
    @Override
    public void bookAppointment(Long residentId, AppointmentDTO appointmentDTO) {
        try {
            User resident = userRepository.findById(residentId)
                    .orElseThrow(() -> new ResourceNotFoundException(HelperAppConstants.HA009));

            User helper = userRepository.findById(appointmentDTO.getHelperId())
                    .orElseThrow(() -> new ResourceNotFoundException(HelperAppConstants.HA010));


            Appointment appointment = appointmentMapper.mapAppointmentDTOtoAppointment(residentId, appointmentDTO);

            boolean isHelperBusy = appointmentRepository.existsByHelperIdAndScheduledAt(appointmentDTO.getHelperId(), appointment.getScheduledAt());
            if(isHelperBusy) {
                throw new HelperAppException(HttpStatus.CONFLICT, HelperAppConstants.HA011);
            }

            appointment.setResident(resident);
            appointment.setHelper(helper);

            appointmentRepository.save(appointment);
        } catch(Exception ex) {
            throw ex;
        }
    }

    /**
     * Fetches Available helpers optionally filtered by skill and dateTime
     * @param skill
     * @param dateTime
     * @return
     */
    @Override
    public List<AvailableHelpersDTO> fetchAllAvailableHelpers(String skill, String dateTime) {
        try {
            return appointmentRepository.fetchAllAvailableHelpers(dateTime != null ? DateUtility.convertStringToLocalDateTime(dateTime) : null, skill);
        } catch(Exception ex) {
            throw ex;
        }
    }
}
