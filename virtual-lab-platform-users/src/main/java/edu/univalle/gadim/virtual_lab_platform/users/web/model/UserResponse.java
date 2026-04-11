package edu.univalle.gadim.virtual_lab_platform.users.web.model;

import edu.univalle.gadim.virtual_lab_platform.users.api.type.UserStatus;
import java.time.LocalDateTime;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Response DTO for user information.
 *
 * <p>This record contains the complete information about a user,
 * including ID, personal details, optional external code, status, and creation date.
 */
@ParametersAreNonnullByDefault
public record UserResponse(
    @Nonnull String id,
    @Nonnull String name,
    @Nonnull String lastName,
    @Nullable String externalCode,
    @Nonnull UserStatus status,
    @Nonnull LocalDateTime createdDate) {
}