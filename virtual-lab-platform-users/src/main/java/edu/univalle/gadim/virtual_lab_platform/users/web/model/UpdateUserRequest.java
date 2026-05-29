package edu.univalle.gadim.virtual_lab_platform.users.web.model;

import edu.univalle.gadim.virtual_lab_platform.users.api.type.UserStatus;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Request DTO for updating an existing user.
 *
 * <p>All fields are optional. Only non-null fields will be applied during the update.
 * If a new password is provided, it will be re-encoded before persistence.
 */
@ParametersAreNonnullByDefault
public record UpdateUserRequest(
    @Nullable String name,
    @Nullable String lastName,
    @Nullable String externalCode,
    @Nullable String password,
    @Nullable UserStatus status) {
}
