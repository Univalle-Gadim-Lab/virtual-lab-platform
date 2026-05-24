package edu.univalle.gadim.virtual_lab_platform.users.api.type;

import javax.annotation.Nonnull;

/**
 * Contract for user-to-role associations.
 *
 * <p>Links a platform user to a specific authorization role. A user may hold
 * multiple roles simultaneously, each represented by a separate {@code UserRole} instance.
 *
 * @see Role
 * @see User
 */
public interface UserRole {
  /**
   * Returns the unique identifier for this role assignment.
   *
   * @return the user role ID, never null
   */
  @Nonnull
  String id();

  /**
   * Returns the ID of the user this role is assigned to.
   *
   * @return the user ID, never null
   */
  @Nonnull
  String userId();

  /**
   * Returns the assigned role.
   *
   * @return the role, never null
   */
  @Nonnull
  Role role();
}
