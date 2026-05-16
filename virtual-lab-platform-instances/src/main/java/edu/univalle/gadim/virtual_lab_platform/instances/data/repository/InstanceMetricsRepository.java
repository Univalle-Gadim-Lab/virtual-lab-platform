package edu.univalle.gadim.virtual_lab_platform.instances.data.repository;

import edu.univalle.gadim.virtual_lab_platform.instances.data.model.InstanceMetricsJpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstanceMetricsRepository extends JpaRepository<InstanceMetricsJpa, String> {

    List<InstanceMetricsJpa> findByInstanceId(String instanceId);
}