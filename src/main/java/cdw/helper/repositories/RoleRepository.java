package cdw.helper.repositories;

import cdw.helper.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Role entity
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String roleName);
}
