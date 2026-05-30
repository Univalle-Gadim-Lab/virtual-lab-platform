package edu.univalle.gadim.virtual_lab_platform.authentication.api.type;

import java.time.LocalDateTime;
import javax.annotation.Nonnull;

/**
 * Contract for refresh token persistence.
 *
 * <p>Represents a long-lived token issued after successful authentication that
 * allows clients to obtain new access tokens without re-authenticating. Refresh
 * tokens are stored in the database so they can be explicitly revoked on logout.
 *
 * <p><b>Implementation Requirements:</b>
 * <ul>
 *   <li>{@link #id()} must never be null or empty</li>
 *   <li>{@link #token()} must be a cryptographically random string</li>
 *   <li>{@link #revoked()} returns {@code true} once the token has been invalidated</li>
 * </ul>
 *
 * @see TokenType
 */
public interface RefreshToken {

  /**
   * Returns the unique identifier for this refresh token.
   *
   * @return the token ID, never null
   */
  @Nonnull
  String id();

  /**
   * Returns the ID of the user this token was issued to.
   *
   * @return the user ID, never null
   */
  @Nonnull
  String userId();

  /**
   * Returns the raw token string used for lookup and validation.
   *
   * @return the token string, never null
   */
  @Nonnull
  String token();

  /**
   * Returns the timestamp after which this token is no longer valid.
   *
   * @return the expiration timestamp, never null
   */
  @Nonnull
  LocalDateTime expiresAt();

  /**
   * Returns whether this token has been revoked (e.g., via logout).
   *
   * @return {@code true} if the token is revoked and must not be accepted
   */
  boolean revoked();

  /**
   * Returns the timestamp when this token was created.
   *
   * @return the creation timestamp, never null
   */
  @Nonnull
  LocalDateTime createdAt();
}
