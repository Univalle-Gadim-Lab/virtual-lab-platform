package edu.univalle.gadim.virtual_lab_platform.users.api.service;

import edu.univalle.gadim.virtual_lab_platform.users.api.type.User;
import java.util.List;
import java.util.Optional;

/**
 * Service contract for user lifecycle operations.
 *
 * <p>Provides methods for creating and retrieving platform users.
 * Implementations are responsible for ID generation, password encoding,
 * and persistence.
 *
 * @see User
 * @see edu.univalle.gadim.virtual_lab_platform.users.operation.UserServiceOperation
 */
public interface UserService {

  /**
   * Creates a new user with the provided information.
   *
   * <p>The user's institutional email address is used as the unique identifier (id).
   * Password will be encoded before persistence.
   *
   * @param user the user data to persist (id is the institutional email, password will be encoded)
   * @return the created user with creation date
   */
  User createUser(User user);

  /**
   * Retrieves a user by their unique identifier (institutional email).
   *
   * @param id the unique user identifier (institutional email)
   * @return the user if found, or {@code Optional.empty()} if not found
   */
  Optional<User> getUserById(String id);

  /**
   * Retrieves all users from the system.
   *
   * @return a list of all users, never null but may be empty
   */
  List<User> getAllUsers();

  /**
   * Updates an existing user's mutable fields.
   *
   * <p>Only non-null fields from {@code user} are applied. If a new password
   * is provided and non-empty, it will be re-encoded before persistence.
   *
   * @param id the unique identifier of the user to update
   * @param user the user data containing fields to update
   * @return the updated user
   * @throws IllegalArgumentException if no user exists with the given ID
   */
  User updateUser(String id, User user);

  /**
   * Marks a user as deleted by transitioning their status to {@code DELETED}.
   *
   * <p>The user must currently be in {@code INACTIVE} status. The user record
   * is retained for historical integrity; only the status field is changed.
   *
   * @param id the unique identifier of the user to delete
   * @throws IllegalArgumentException if no user exists with the given ID
   * @throws IllegalStateException if the user is not currently {@code INACTIVE}
   */
  void deleteUser(String id);
}
