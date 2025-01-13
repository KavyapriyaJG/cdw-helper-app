package com.cdw.helper.app.helper.repositories;

import com.cdw.helper.app.helper.entities.HelperDetails;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface for HelperDetails Repository
 */
public interface HelperDetailsRepository extends JpaRepository<HelperDetails, Long> {
}
