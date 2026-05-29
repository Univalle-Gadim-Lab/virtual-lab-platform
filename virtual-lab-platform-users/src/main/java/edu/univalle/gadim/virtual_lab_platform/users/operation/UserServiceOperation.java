package edu.univalle.gadim.virtual_lab_platform.users.operation;

import edu.univalle.gadim.virtual_lab_platform.commons.type.UniqueIdGenerator;
import edu.univalle.gadim.virtual_lab_platform.users.api.service.UserService;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.User;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.UserStatus;
import edu.univalle.gadim.virtual_lab_platform.users.data.model.UserJpa;
import edu.univalle.gadim.virtual_lab_platform.users.data.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation of UserService providing operations for managing users.
 *
 * <p>This service handles the creation and retrieval of user information,
 * ensuring proper data persistence and access.
 */
@Service
@ParametersAreNonnullByDefault
public class UserServiceOperation implements UserService {

  private final UniqueIdGenerator idGenerator;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserServiceOperation(
      UniqueIdGenerator idGenerator,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder) {
    this.idGenerator = idGenerator;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Creates a new user with the provided information.
   *
   * <p>This method generates a unique ID for the user, encodes the password,
   * sets the creation date, and persists the user to the database.
   *
   * @param user the user information to create
   * @return the created User instance with generated ID and creation date
   */
  @Nonnull
  @Override
  public User createUser(User user) {
    UserJpa userJpa = UserJpa.builder()
        .id(idGenerator.generate())
        .name(user.name())
        .lastName(user.lastName())
        .externalCode(user.externalCode().orElse(null))
        .password(passwordEncoder.encode(user.password()))
        .status(user.status())
        .createdDate(LocalDateTime.now())
        .build();

    return userRepository.save(userJpa);
  }

  /**
   * Retrieves a user by their unique ID.
   *
   * @param id the unique identifier of the user
   * @return an Optional containing the User if found, empty otherwise
   */
  @Nonnull
  @Override
  public Optional<User> getUserById(String id) {
    return userRepository.findById(id).map(User.class::cast);
  }

  /**
   * Retrieves a user by their username (name field).
   *
   * @param username the username to search for
   * @return an Optional containing the User if found, empty otherwise
   */
  @Nonnull
  @Override
  public Optional<User> getUserByUsername(String username) {
    return userRepository.findByName(username).map(User.class::cast);
  }

  /**
   * Retrieves all users from the system.
   *
   * @return a list of all users
   */
  @Nonnull
  @Override
  public List<User> getAllUsers() {
    return userRepository.findAll().stream()
        .map(User.class::cast)
        .toList();
  }

  @Nonnull
  @Override
  public User updateUser(String id, User user) {
    final var existing =
        userRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

    existing.setName(user.name());
    existing.setLastName(user.lastName());
    existing.setExternalCode(user.externalCode().orElse(null));
    existing.setStatus(user.status());

    final var newPassword = user.password();
    if (newPassword != null && !newPassword.isEmpty()) {
      existing.setPassword(passwordEncoder.encode(newPassword));
    }

    return userRepository.save(existing);
  }

  @Override
  public void deleteUser(String id) {
    final var user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

    if (user.status() != UserStatus.INACTIVE) {
      throw new IllegalStateException(
          "User must be INACTIVE before deletion. Current status: " + user.status());
    }

    user.setStatus(UserStatus.DELETED);
    userRepository.save(user);
  }
}
