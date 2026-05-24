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
   * @param user the user data to persist (ID and password will be generated/encoded)
   * @return the created user with generated ID and creation date
   */
  User createUser(User user);

  /**
   * Retrieves a user by their unique identifier.
   *
   * @param id the unique user identifier
   * @return the user if found, or {@code Optional.empty()} if not found
   */
  Optional<User> getUserById(String id);

  /**
   * Retrieves a user by their username.
   *
   * @param username the username to search for
   * @return the user if found, or {@code Optional.empty()} if not found
   */
  Optional<User> getUserByUsername(String username);

  /**
   * Retrieves all users from the system.
   *
   * @return a list of all users, never null but may be empty
   */
  List<User> getAllUsers();
}
