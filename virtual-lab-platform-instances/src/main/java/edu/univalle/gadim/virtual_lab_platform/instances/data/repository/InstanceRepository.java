package edu.univalle.gadim.virtual_lab_platform.instances.data.repository;

import edu.univalle.gadim.virtual_lab_platform.instances.data.model.InstanceJpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing InstanceJpa entities.
 *
 * <p>Provides data access operations for virtual lab instances.
 */
@Repository
public interface InstanceRepository extends JpaRepository<InstanceJpa, String> {

  /**
   * Finds all instances associated with a specific user through the instance_user join table.
   *
   * @param userId the user ID
   * @return list of instances belonging to the user
   */
  @Query(
      "SELECT i FROM InstanceJpa i JOIN InstanceUserJpa iu ON i.id = iu.instanceId WHERE iu.userId = :userId")
  List<InstanceJpa> findByUserId(@Param("userId") String userId);
}