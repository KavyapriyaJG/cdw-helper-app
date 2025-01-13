package com.cdw.helper.app.common.repositories;

import com.cdw.helper.app.common.dto.AvailableHelpersDTO;
import com.cdw.helper.app.common.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.cdw.helper.app.common.constants.QueryConstants.HELPERS_NOT_IN_APPOINTMENTS;

/**
 * Repository for Appointment Entity
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByHelperId(Long helperId);

    boolean existsByHelperIdAndScheduledAt(Long id, LocalDateTime scheduledAt);

    @Query(nativeQuery = true, value= HELPERS_NOT_IN_APPOINTMENTS)
    List<AvailableHelpersDTO> fetchAllAvailableHelpers(@Param("dateTime") LocalDateTime dateTime, @Param("skill") String skill);
}
