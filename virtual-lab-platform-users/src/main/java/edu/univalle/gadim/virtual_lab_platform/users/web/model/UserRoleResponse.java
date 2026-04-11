package edu.univalle.gadim.virtual_lab_platform.users.web.model;

import edu.univalle.gadim.virtual_lab_platform.users.api.type.Role;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Response DTO for user role information.
 *
 * <p>This record contains the ID, user ID, and role for a user role assignment.
 */
@ParametersAreNonnullByDefault
public record UserRoleResponse(
    @Nonnull String id,
    @Nonnull String userId,
    @Nonnull Role role) {
}