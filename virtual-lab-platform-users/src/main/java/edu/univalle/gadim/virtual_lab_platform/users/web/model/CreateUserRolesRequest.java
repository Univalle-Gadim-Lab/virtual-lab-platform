package edu.univalle.gadim.virtual_lab_platform.users.web.model;

import edu.univalle.gadim.virtual_lab_platform.users.api.type.Role;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Request DTO for creating multiple user roles.
 *
 * <p>This record contains the user ID and the list of roles to assign.
 */
@ParametersAreNonnullByDefault
public record CreateUserRolesRequest(
    @Nonnull String userId,
    @Nonnull List<Role> roles) {
}