package edu.univalle.gadim.virtual_lab_platform.authentication.web.ops;

import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.AuthenticatedUserResponse;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.LoginRequest;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.LoginResponse;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.LogoutRequest;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.RefreshTokenRequest;
import javax.annotation.Nonnull;

/**
 * Web service operations contract for authentication management.
 *
 * <p>Defines one method per web endpoint exposed by the authentication REST API.
 * Implementations perform request-to-domain translation and domain-to-response mapping.
 *
 * <h2>Endpoints</h2>
 * <ul>
 *   <li>{@code POST /api/auth/login} — authenticate and obtain tokens</li>
 *   <li>{@code POST /api/auth/refresh} — exchange refresh token for new access token</li>
 *   <li>{@code POST /api/auth/logout} — revoke refresh token</li>
 *   <li>{@code GET /api/auth/me} — retrieve current user info</li>
 * </ul>
 */
public interface AuthWsOps {

  /**
   * Authenticates a user with the given credentials and returns a token pair.
   *
   * @param request the login request containing username and password
   * @return the login response with access token, refresh token, and metadata
   * @throws IllegalArgumentException if the user is not found or credentials are invalid
   * @throws IllegalStateException    if the user account is not in {@code ACTIVE} status
   */
  @Nonnull
  LoginResponse login(@Nonnull LoginRequest request);

  /**
   * Exchanges a valid refresh token for a new access token.
   *
   * @param request the refresh token request containing the refresh token
   * @return the login response with the new access token
   * @throws IllegalArgumentException if the refresh token is not found
   * @throws IllegalStateException    if the refresh token is revoked or expired
   */
  @Nonnull
  LoginResponse refresh(@Nonnull RefreshTokenRequest request);

  /**
   * Revokes the given refresh token, preventing future use.
   *
   * @param request the logout request containing the refresh token to revoke
   * @throws IllegalArgumentException if the refresh token is not found
   */
  void logout(@Nonnull LogoutRequest request);

  /**
   * Retrieves the current authenticated user's identity and roles.
   *
   * @param userId the user ID extracted from the JWT by the security filter
   * @return the authenticated user response with identity and roles
   * @throws IllegalArgumentException if no user exists with the given ID
   */
  @Nonnull
  AuthenticatedUserResponse me(@Nonnull String userId);
}
