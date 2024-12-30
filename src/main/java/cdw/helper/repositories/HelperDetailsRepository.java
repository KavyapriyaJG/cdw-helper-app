package cdw.helper.repositories;

import cdw.helper.entities.HelperDetails;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface for HelperDetails Repository
 */
public interface HelperDetailsRepository extends JpaRepository<HelperDetails, Long> {
}
