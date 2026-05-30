package edu.univalle.gadim.virtual_lab_platform.authentication.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Response DTO containing issued authentication tokens.
 *
 * @param accessToken  the JWT access token for API authorization
 * @param refreshToken the JWT refresh token for obtaining new access tokens
 * @param tokenType    the token type prefix (always {@code "Bearer"})
 * @param expiresIn    the access token lifetime in milliseconds
 */
@ParametersAreNonnullByDefault
public record LoginResponse(
    @Nonnull @JsonProperty("access_token") String accessToken,
    @Nonnull @JsonProperty("refresh_token") String refreshToken,
    @Nonnull @JsonProperty("token_type") String tokenType,
    @JsonProperty("expires_in") long expiresIn) {}
