package cdw.helper.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * HelperDetails entity that encapsulates the data of a Helper/ Technician
 */
@Data
@Entity
public class HelperDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String specialization;

    private float rating;

    private float hourlyRate;
}
