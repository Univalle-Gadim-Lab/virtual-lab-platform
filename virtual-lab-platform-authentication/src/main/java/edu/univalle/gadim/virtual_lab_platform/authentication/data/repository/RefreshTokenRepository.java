package edu.univalle.gadim.virtual_lab_platform.authentication.data.repository;

import edu.univalle.gadim.virtual_lab_platform.authentication.data.model.RefreshTokenJpa;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for {@link RefreshTokenJpa} persistence.
 *
 * <p>Provides standard CRUD operations plus custom lookups by token string
 * and user ID.
 *
 * @see RefreshTokenJpa
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenJpa, String> {

  /**
   * Finds a refresh token by its raw token string.
   *
   * @param token the token string to search for
   * @return the refresh token if found, or {@code Optional.empty()} if not found
   */
  Optional<RefreshTokenJpa> findByToken(String token);

  /**
   * Finds all refresh tokens issued to the specified user.
   *
   * @param userId the user ID to search for
   * @return the list of refresh tokens, never null but may be empty
   */
  List<RefreshTokenJpa> findByUserId(String userId);
}
