package edu.univalle.gadim.virtual_lab_platform.authentication.web.model;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Request DTO for user logout.
 *
 * @param refreshToken the refresh token to revoke
 */
@ParametersAreNonnullByDefault
public record LogoutRequest(@Nonnull String refreshToken) {}
