package edu.univalle.gadim.virtual_lab_platform.users.data.repository;

import edu.univalle.gadim.virtual_lab_platform.users.data.model.UserJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserJpa, String> {
    Optional<UserJpa> findByName(String name);
}
