package edu.univalle.gadim.virtual_lab_platform.users.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
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
 * Fields with {@code null} values (such as {@code externalCode}) are omitted from
 * the serialized JSON representation via {@link JsonInclude#value()} set to
 * {@link com.fasterxml.jackson.annotation.JsonInclude.Include#NON_NULL}.
 *
 * @param id the unique user identifier
 * @param name the user's first name
 * @param lastName the user's last name
 * @param externalCode optional external identifier (e.g., student code); omitted from JSON if null
 * @param status the user's account lifecycle state
 * @param createdDate the timestamp when the user account was created
 */
@ParametersAreNonnullByDefault
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(
    @Nonnull String id,
    @Nonnull String name,
    @Nonnull String lastName,
    @Nullable String externalCode,
    @Nonnull UserStatus status,
    @Nonnull LocalDateTime createdDate) {
}
