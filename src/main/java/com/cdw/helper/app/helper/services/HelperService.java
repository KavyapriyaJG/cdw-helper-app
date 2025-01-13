package com.cdw.helper.app.helper.services;

import com.cdw.helper.app.common.entities.Appointment;

import java.util.List;

/**
 * Interface for Helper Service Operations
 */
public interface HelperService {
    List<Appointment> fetchAppointments(Long helperId);
}
