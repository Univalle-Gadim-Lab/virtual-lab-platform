package edu.univalle.gadim.virtual_lab_platform.authentication.web.model;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Request DTO for refreshing an access token.
 *
 * @param refreshToken the refresh token to exchange for a new access token
 */
@ParametersAreNonnullByDefault
public record RefreshTokenRequest(@Nonnull String refreshToken) {}
