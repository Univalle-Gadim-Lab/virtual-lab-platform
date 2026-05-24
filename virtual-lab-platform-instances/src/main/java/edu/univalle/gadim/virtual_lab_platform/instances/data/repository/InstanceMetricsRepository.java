package edu.univalle.gadim.virtual_lab_platform.instances.data.repository;

import edu.univalle.gadim.virtual_lab_platform.instances.data.model.InstanceMetricsJpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for {@link InstanceMetricsJpa} persistence.
 *
 * <p>Provides standard CRUD operations plus a custom lookup by instance ID.
 *
 * @see InstanceMetricsJpa
 */
@Repository
public interface InstanceMetricsRepository extends JpaRepository<InstanceMetricsJpa, String> {

    /**
     * Finds all metrics records for the specified instance.
     *
     * @param instanceId the instance ID to search for
     * @return the list of metrics records, never null but may be empty
     */
    List<InstanceMetricsJpa> findByInstanceId(String instanceId);
}