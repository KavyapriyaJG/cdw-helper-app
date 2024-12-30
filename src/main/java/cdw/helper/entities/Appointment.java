package cdw.helper.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Appointment entity that encapsulates the data of an appointment
 */
@Data
@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime scheduledAt;

    @ManyToOne
    @JoinColumn(name="resident_id")
    private User resident;

    @ManyToOne
    @JoinColumn(name="helper_id")
    private User helper;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
