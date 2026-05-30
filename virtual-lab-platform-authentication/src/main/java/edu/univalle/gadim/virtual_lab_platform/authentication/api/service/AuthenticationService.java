package edu.univalle.gadim.virtual_lab_platform.authentication.api.service;

/**
 * Service contract for authentication lifecycle operations.
 *
 * <p>Provides methods for user login, token refresh, logout, and access token
 * validation. Implementations are responsible for credential verification,
 * token generation, and refresh token persistence.
 *
 * @see TokenService
 */
public interface AuthenticationService {

  /**
   * Authenticates a user with the given credentials and issues token pair.
   *
   * @param username the username to authenticate
   * @param password the plaintext password to verify
   * @return the authentication result containing access and refresh tokens
   * @throws IllegalArgumentException if the user is not found or credentials are invalid
   * @throws IllegalStateException    if the user account is not in {@code ACTIVE} status
   */
  AuthenticationResult login(String username, String password);

  /**
   * Exchanges a valid refresh token for a new access token.
   *
   * @param refreshToken the refresh token string
   * @return the authentication result containing the new access token
   * @throws IllegalArgumentException if the refresh token is not found
   * @throws IllegalStateException    if the refresh token is revoked or expired
   */
  AuthenticationResult refresh(String refreshToken);

  /**
   * Revokes the given refresh token, preventing future use.
   *
   * @param refreshToken the refresh token string to revoke
   * @throws IllegalArgumentException if the refresh token is not found
   */
  void logout(String refreshToken);

  /**
   * Validates whether the given access token is well-formed and not expired.
   *
   * @param accessToken the access token string to validate
   * @return {@code true} if the token is valid
   */
  boolean validateAccessToken(String accessToken);

  /**
   * Authentication result containing issued tokens and their metadata.
   *
   * @param accessToken  the JWT access token
   * @param refreshToken the JWT refresh token
   * @param tokenType    the token type prefix (always {@code "Bearer"})
   * @param expiresIn    the access token lifetime in milliseconds
   */
  record AuthenticationResult(
      String accessToken,
      String refreshToken,
      String tokenType,
      long expiresIn) {}
}
