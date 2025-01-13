package com.cdw.helper.app.resident.services;

import com.cdw.helper.app.common.constants.HelperAppConstants;
import com.cdw.helper.app.common.dto.AppointmentDTO;
import com.cdw.helper.app.common.dto.AvailableHelpersDTO;
import com.cdw.helper.app.common.entities.Appointment;
import com.cdw.helper.app.common.entities.User;
import com.cdw.helper.app.common.exceptions.HelperAppException;
import com.cdw.helper.app.common.exceptions.ResourceNotFoundException;
import com.cdw.helper.app.common.mappers.AppointmentMapper;
import com.cdw.helper.app.common.repositories.AppointmentRepository;
import com.cdw.helper.app.common.repositories.UserRepository;
import com.cdw.helper.app.common.utilities.DateUtility;
import com.cdw.helper.app.helper.repositories.HelperDetailsRepository;
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
