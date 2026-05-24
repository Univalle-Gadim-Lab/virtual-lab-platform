package edu.univalle.gadim.virtual_lab_platform.instances.data.repository;

import edu.univalle.gadim.virtual_lab_platform.instances.data.model.InstanceUserJpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for {@link InstanceUserJpa} persistence.
 *
 * <p>Provides standard CRUD operations plus custom lookups by user ID and instance ID.
 *
 * @see InstanceUserJpa
 */
@Repository
public interface InstanceUserRepository extends JpaRepository<InstanceUserJpa, String> {

    /**
     * Finds all instance-user associations for the specified user.
     *
     * @param userId the user ID to search for
     * @return the list of associations, never null but may be empty
     */
    List<InstanceUserJpa> findByUserId(String userId);

    /**
     * Finds all instance-user associations for the specified instance.
     *
     * @param instanceId the instance ID to search for
     * @return the list of associations, never null but may be empty
     */
    List<InstanceUserJpa> findByInstanceId(String instanceId);
}