package cdw.helper.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * UserDetails entity that encapsulates the necessary data of an actor in Helper application
 */
@Data
@Entity
@Table(name="user_details")
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String gender;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "user_details_helper_details",
            joinColumns = {@JoinColumn(name = "user_details_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "helper_details_id", referencedColumnName = "id")}
    )
    private HelperDetails helperDetails;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "user_details_resident_details",
            joinColumns = {@JoinColumn(name = "user_details_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "resident_details_id", referencedColumnName = "id")}
    )
    private ResidentDetails residentDetails;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
