package edu.univalle.gadim.virtual_lab_platform.users.api.service;

import edu.univalle.gadim.virtual_lab_platform.users.api.type.Role;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.UserRole;
import java.util.List;

/**
 * Service contract for user role assignment operations.
 *
 * <p>Provides methods for assigning roles to users and retrieving role
 * assignments. A user may hold multiple roles simultaneously.
 *
 * @see UserRole
 * @see Role
 * @see edu.univalle.gadim.virtual_lab_platform.users.operation.UserRolesServiceOperation
 */
public interface UserRoleService {

  /**
   * Assigns a single role to the specified user.
   *
   * @param userId the ID of the user to assign the role to
   * @param role the role to assign
   * @return the created user role assignment
   * @throws IllegalArgumentException if the user does not exist
   */
  UserRole createUserRole(String userId, Role role);

  /**
   * Assigns multiple roles to the specified user.
   *
   * @param userId the ID of the user to assign the roles to
   * @param roles the list of roles to assign
   * @return the list of created user role assignments
   * @throws IllegalArgumentException if the user does not exist
   */
  List<UserRole> createUserRoles(String userId, List<Role> roles);

  /**
   * Retrieves all roles assigned to the specified user.
   *
   * @param userId the ID of the user to retrieve roles for
   * @return the list of role assignments for the user, never null but may be empty
   */
  List<UserRole> getRoleByUserId(String userId);

  /**
   * Removes a role assignment by its unique identifier.
   *
   * @param id the unique identifier of the role assignment to remove
   * @throws IllegalArgumentException if no role assignment exists with the given ID
   */
  void deleteUserRole(String id);
}
