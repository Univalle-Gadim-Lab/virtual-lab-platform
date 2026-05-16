package edu.univalle.gadim.virtual_lab_platform.instances.data.repository;

import edu.univalle.gadim.virtual_lab_platform.instances.data.model.InstanceUserJpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstanceUserRepository extends JpaRepository<InstanceUserJpa, String> {

    List<InstanceUserJpa> findByUserId(String userId);

    List<InstanceUserJpa> findByInstanceId(String instanceId);
}