package edu.univalle.gadim.virtual_lab_platform.users.operation;

import edu.univalle.gadim.virtual_lab_platform.commons.type.UniqueIdGenerator;
import edu.univalle.gadim.virtual_lab_platform.users.api.service.UserRoleService;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.Role;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.UserRole;
import edu.univalle.gadim.virtual_lab_platform.users.data.model.UserJpa;
import edu.univalle.gadim.virtual_lab_platform.users.data.model.UserRoleJpa;
import edu.univalle.gadim.virtual_lab_platform.users.data.repository.UserRepository;
import edu.univalle.gadim.virtual_lab_platform.users.data.repository.UserRoleRepository;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.stereotype.Service;

/**
 * Implementation of UserRoleService providing operations for managing user roles.
 *
 * <p>This service handles the creation and retrieval of user roles, ensuring proper association
 * between users and their assigned roles.
 */
@Service
@ParametersAreNonnullByDefault
public class UserRolesServiceOperation implements UserRoleService {

  private static final String USER_NOT_FOUND = "User with ID ";

  private final UniqueIdGenerator idGenerator;
  private final UserRepository userRepository;
  private final UserRoleRepository userRoleRepository;

  public UserRolesServiceOperation(
      UniqueIdGenerator idGenerator,
      UserRepository userRepository,
      UserRoleRepository userRoleRepository) {
    this.idGenerator = idGenerator;
    this.userRepository = userRepository;
    this.userRoleRepository = userRoleRepository;
  }

  /**
   * Creates a new user role for the specified user and role.
   *
   * @param userId the ID of the user to assign the role to
   * @param role the role to assign
   * @return the created UserRole instance
   * @throws IllegalArgumentException if the user does not exist
   */
  @Nonnull
  @Override
  public UserRole createUserRole(String userId, Role role) {
    final var user = requireUserById(userId);

    UserRoleJpa userRoleJpa =
        UserRoleJpa.builder().id(idGenerator.generate()).userId(user.id()).role(role).build();

    return userRoleRepository.save(userRoleJpa);
  }

  /**
   * Creates multiple user roles for the specified user.
   *
   * @param userId the ID of the user to assign the roles to
   * @param roles the list of roles to assign
   * @return the list of created UserRole instances
   * @throws IllegalArgumentException if the user does not exist
   */
  @Nonnull
  @Override
  public List<UserRole> createUserRoles(String userId, List<Role> roles) {
    final var user = requireUserById(userId);

    List<UserRoleJpa> userRoleJpas =
        roles.stream()
            .map(
                role ->
                    UserRoleJpa.builder()
                        .id(idGenerator.generate())
                        .userId(user.id())
                        .role(role)
                        .build())
            .toList();

    return userRoleRepository.saveAll(userRoleJpas).stream().map(UserRole.class::cast).toList();
  }

  /**
   * Retrieves all role assignments for the specified user.
   *
   * @param userId the ID of the user whose roles to retrieve
   * @return a list of role assignments, never null but may be empty if the user has no roles
   */
  @Nonnull
  @Override
  public List<UserRole> getRoleByUserId(String userId) {
    return userRoleRepository.findByUserId(userId).stream().map(UserRole.class::cast).toList();
  }

  private UserJpa requireUserById(String userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(
            () -> new IllegalArgumentException(USER_NOT_FOUND + userId + " does not exist"));
  }
}
