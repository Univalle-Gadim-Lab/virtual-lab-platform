package edu.univalle.gadim.virtual_lab_platform.users.data.repository;

import edu.univalle.gadim.virtual_lab_platform.users.data.model.UserRoleJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleJpa, String> {
    List<UserRoleJpa> findByUserId(String userId);
}
