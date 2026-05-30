package edu.univalle.gadim.virtual_lab_platform.authentication.web.controller;

import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.AuthenticatedUserResponse;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.LoginRequest;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.LoginResponse;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.LogoutRequest;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.RefreshTokenRequest;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.ops.AuthWsOps;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for authentication operations.
 *
 * <p>Provides endpoints for user login, token refresh, logout, and current user
 * retrieval. All operations are delegated to {@link AuthWsOps}, keeping this class
 * as a thin HTTP adapter.
 *
 * <h2>Endpoints</h2>
 * <ul>
 *   <li>{@code POST /api/auth/login} — authenticate and obtain tokens</li>
 *   <li>{@code POST /api/auth/refresh} — exchange refresh token for new access token</li>
 *   <li>{@code POST /api/auth/logout} — revoke refresh token</li>
 *   <li>{@code GET /api/auth/me} — retrieve current user info</li>
 * </ul>
 *
 * @see AuthWsOps
 */
@RestController
@RequestMapping("/api/auth")
@ParametersAreNonnullByDefault
public class AuthController {

  private final AuthWsOps authWsOps;

  /**
   * Constructs a new {@code AuthController} with the required operations dependency.
   *
   * @param authWsOps the web service operations interface for authentication
   */
  public AuthController(AuthWsOps authWsOps) {
    this.authWsOps = authWsOps;
  }

  /**
   * Authenticates a user and returns an access token and refresh token.
   *
   * @param request the login request containing username and password
   * @return a {@code 200 OK} response with the token pair
   */
  @PostMapping("/login")
  @Nonnull
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
    return ResponseEntity.ok(authWsOps.login(request));
  }

  /**
   * Exchanges a valid refresh token for a new access token.
   *
   * @param request the refresh token request containing the refresh token
   * @return a {@code 200 OK} response with the new access token
   */
  @PostMapping("/refresh")
  @Nonnull
  public ResponseEntity<LoginResponse> refresh(
      @RequestBody RefreshTokenRequest request) {
    return ResponseEntity.ok(authWsOps.refresh(request));
  }

  /**
   * Revokes the given refresh token, preventing future use.
   *
   * @param request the logout request containing the refresh token to revoke
   * @return a {@code 204 No Content} response
   */
  @PostMapping("/logout")
  @Nonnull
  public ResponseEntity<Void> logout(@RequestBody LogoutRequest request) {
    authWsOps.logout(request);
    return ResponseEntity.noContent().build();
  }

  /**
   * Retrieves the current authenticated user's identity and roles.
   *
   * <p>The user ID is extracted from the JWT by the security filter and set
   * as a request attribute.
   *
   * @return a {@code 200 OK} response with the authenticated user info
   */
  @GetMapping("/me")
  @Nonnull
  public ResponseEntity<AuthenticatedUserResponse> me() {
    final var userId = org.springframework.security.core.context.SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getName();
    return ResponseEntity.ok(authWsOps.me(userId));
  }
}
