package edu.univalle.gadim.virtual_lab_platform.authentication.web.model;

import edu.univalle.gadim.virtual_lab_platform.users.api.type.Role;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Response DTO containing the authenticated user's identity and roles.
 *
 * @param id       the unique user identifier
 * @param name     the user's first name
 * @param lastName the user's last name
 * @param roles    the user's authorization roles
 */
@ParametersAreNonnullByDefault
public record AuthenticatedUserResponse(
    @Nonnull String id,
    @Nonnull String name,
    @Nonnull String lastName,
    @Nonnull Set<Role> roles) {}
