package edu.univalle.gadim.virtual_lab_platform.users.web.model;

import edu.univalle.gadim.virtual_lab_platform.users.api.type.UserStatus;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Request DTO for creating a new user.
 *
 * <p>This record contains the necessary information to create a user,
 * including personal details, optional external code, password, and status.
 */
@ParametersAreNonnullByDefault
public record CreateUserRequest(
    @Nonnull String name,
    @Nonnull String lastName,
    @Nullable String externalCode,
    @Nonnull String password,
    @Nonnull UserStatus status) {
}