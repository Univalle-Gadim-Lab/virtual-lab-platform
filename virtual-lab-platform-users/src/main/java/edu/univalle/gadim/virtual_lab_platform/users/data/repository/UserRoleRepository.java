package edu.univalle.gadim.virtual_lab_platform.users.data.repository;

import edu.univalle.gadim.virtual_lab_platform.users.data.model.UserRoleJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data repository for {@link UserRoleJpa} persistence.
 *
 * <p>Provides standard CRUD operations plus a custom lookup by user ID.
 *
 * @see UserRoleJpa
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleJpa, String> {
    /**
     * Finds all role assignments for the specified user.
     *
     * @param userId the user ID to search for
     * @return the list of role assignments, never null but may be empty
     */
    List<UserRoleJpa> findByUserId(String userId);
}
