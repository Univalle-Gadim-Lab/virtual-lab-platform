package edu.univalle.gadim.virtual_lab_platform.users.web.model;

import edu.univalle.gadim.virtual_lab_platform.users.api.type.Role;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Request DTO for creating a new user role.
 *
 * <p>This record contains the user ID and the role to assign.
 */
@ParametersAreNonnullByDefault
public record CreateUserRoleRequest(
    @Nonnull String userId,
    @Nonnull Role role) {
}