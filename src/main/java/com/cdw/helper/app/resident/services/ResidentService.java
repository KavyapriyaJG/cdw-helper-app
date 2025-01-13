package com.cdw.helper.app.resident.services;

import com.cdw.helper.app.common.dto.AppointmentDTO;
import com.cdw.helper.app.common.dto.AvailableHelpersDTO;

import java.util.List;

/**
 * Interface for Resident Service Operations
 */
public interface ResidentService {
    void bookAppointment(Long residentId, AppointmentDTO appointmentDTO);
    List<AvailableHelpersDTO> fetchAllAvailableHelpers(String skill, String dateTime);
}
