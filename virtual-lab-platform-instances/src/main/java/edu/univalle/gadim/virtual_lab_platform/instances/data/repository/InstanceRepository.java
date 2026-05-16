package edu.univalle.gadim.virtual_lab_platform.instances.data.repository;

import edu.univalle.gadim.virtual_lab_platform.instances.data.model.InstanceJpa;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstanceRepository extends JpaRepository<InstanceJpa, String> {

    List<InstanceJpa> findByUserId(String userId);
}