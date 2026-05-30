package edu.univalle.gadim.virtual_lab_platform.authentication.api.service;

import edu.univalle.gadim.virtual_lab_platform.users.api.type.Role;
import java.util.List;

/**
 * Service contract for JWT token lifecycle operations.
 *
 * <p>Provides methods for generating, parsing, and validating JSON Web Tokens.
 * Access tokens carry user identity and roles; refresh tokens carry only user identity.
 *
 * @see edu.univalle.gadim.virtual_lab_platform.authentication.api.service.AuthenticationService
 */
public interface TokenService {

  /**
   * Generates a short-lived access token carrying the user's identity and roles.
   *
   * @param userId   the user's unique identifier (stored in the {@code sub} claim)
   * @param username the user's username
   * @param roles    the user's authorization roles
   * @return the signed JWT string
   */
  String generateAccessToken(String userId, String username, List<Role> roles);

  /**
   * Generates a long-lived refresh token for the specified user.
   *
   * @param userId the user's unique identifier
   * @return the signed JWT string
   */
  String generateRefreshToken(String userId);

  /**
   * Validates the given access token: checks signature, expiration, and token type.
   *
   * @param token the JWT string to validate
   * @return {@code true} if the token is valid and not expired
   */
  boolean validateAccessToken(String token);

  /**
   * Extracts the user ID ({@code sub} claim) from the given token.
   *
   * @param token the JWT string
   * @return the user ID
   * @throws io.jsonwebtoken.JwtException if the token is malformed or signature is invalid
   */
  String extractUserId(String token);

  /**
   * Extracts the username from the given access token.
   *
   * @param token the JWT string
   * @return the username
   * @throws io.jsonwebtoken.JwtException if the token is malformed or signature is invalid
   */
  String extractUsername(String token);

  /**
   * Extracts the roles claim from the given access token.
   *
   * @param token the JWT string
   * @return the list of roles, never null but may be empty
   * @throws io.jsonwebtoken.JwtException if the token is malformed or signature is invalid
   */
  List<Role> extractRoles(String token);
}
