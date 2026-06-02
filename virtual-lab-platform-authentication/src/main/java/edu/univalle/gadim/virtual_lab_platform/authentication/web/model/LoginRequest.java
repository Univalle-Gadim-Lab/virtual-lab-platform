package edu.univalle.gadim.virtual_lab_platform.authentication.web.model;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Request DTO for user login.
 *
 * @param email the institutional email address to authenticate
 * @param password the plaintext password to verify
 */
@ParametersAreNonnullByDefault
public record LoginRequest(@Nonnull String email, @Nonnull String password) {}
