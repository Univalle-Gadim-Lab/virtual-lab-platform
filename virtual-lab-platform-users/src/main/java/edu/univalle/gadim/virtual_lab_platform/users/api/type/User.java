package edu.univalle.gadim.virtual_lab_platform.users.api.type;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.annotation.Nonnull;

/**
 * Contract for platform user identities.
 *
 * <p>Exposes user metadata including personal information, credentials,
 * lifecycle status, and creation timestamp. JPA entities implement this
 * interface directly, unifying persistence and domain models.
 *
 * <p><b>Implementation Requirements:</b>
 * <ul>
 *   <li>{@link #id()} must never be null or empty</li>
 *   <li>{@link #externalCode()} returns {@code Optional.empty()} when no external code is assigned</li>
 *   <li>{@link #status()} must reflect the current lifecycle state</li>
 * </ul>
 *
 * @see UserStatus
 * @see UserRole
 */
public interface User {
  /**
   * Returns the unique identifier for this user.
   *
   * @return the user ID, never null
   */
  @Nonnull
  String id();

  /**
   * Returns the user's first name.
   *
   * @return the first name, never null
   */
  @Nonnull
  String name();

  /**
   * Returns the user's last name.
   *
   * @return the last name, never null
   */
  @Nonnull
  String lastName();

  /**
   * Returns an optional external code associated with the user, such as a student ID.
   *
   * @return the external code, or {@code Optional.empty()} if not assigned
   */
  @Nonnull
  Optional<String> externalCode();

  /**
   * Returns the user's password.
   *
   * @return the password (hashed for stored entities), never null
   */
  @Nonnull
  String password();

  /**
   * Returns the current lifecycle status of this user.
   *
   * @return the user status, never null
   */
  @Nonnull
  UserStatus status();

  /**
   * Returns the date and time when this user was created.
   *
   * @return the creation timestamp, never null
   */
  @Nonnull
  LocalDateTime createdDate();
}
