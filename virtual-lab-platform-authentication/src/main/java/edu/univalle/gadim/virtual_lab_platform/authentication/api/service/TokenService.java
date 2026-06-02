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
   * @param userId   the user's unique identifier, institutional email (stored in the {@code sub} claim)
   * @param name     the user's full name (stored in the {@code name} claim)
   * @param roles    the user's authorization roles
   * @return the signed JWT string
   */
  String generateAccessToken(String userId, String name, List<Role> roles);

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
   * Extracts the user's full name from the given access token.
   *
   * @param token the JWT string
   * @return the user's full name
   * @throws io.jsonwebtoken.JwtException if the token is malformed or signature is invalid
   */
  String extractName(String token);

  /**
   * Extracts the roles claim from the given access token.
   *
   * @param token the JWT string
   * @return the list of roles, never null but may be empty
   * @throws io.jsonwebtoken.JwtException if the token is malformed or signature is invalid
   */
  List<Role> extractRoles(String token);
}
