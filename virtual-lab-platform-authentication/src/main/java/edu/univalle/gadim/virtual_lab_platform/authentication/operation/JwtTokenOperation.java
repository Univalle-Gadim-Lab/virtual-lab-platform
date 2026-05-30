package edu.univalle.gadim.virtual_lab_platform.authentication.operation;

import edu.univalle.gadim.virtual_lab_platform.authentication.api.service.TokenService;
import edu.univalle.gadim.virtual_lab_platform.authentication.api.type.TokenType;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link TokenService} backed by the JJWT library.
 *
 * <p>Generates and validates HMAC-SHA256 signed JWTs. Access tokens carry
 * user identity, username, and roles; refresh tokens carry only user identity.
 * Tokens include a {@code type} claim to prevent cross-use.
 *
 * @see TokenService
 * @see edu.univalle.gadim.virtual_lab_platform.authentication.api.type.TokenType
 */
@Service
@ParametersAreNonnullByDefault
public class JwtTokenOperation implements TokenService {

  private static final String CLAIM_USERNAME = "username";
  private static final String CLAIM_ROLES = "roles";
  private static final String CLAIM_TYPE = "type";

  private final SecretKey signingKey;
  private final long accessTokenExpirationMs;
  private final long refreshTokenExpirationMs;

  /**
   * Constructs a new {@code JwtTokenOperation} with the specified configuration.
   *
   * @param secret                   the HMAC-SHA256 secret key (minimum 256 bits)
   * @param accessTokenExpirationMs  the access token lifetime in milliseconds
   * @param refreshTokenExpirationMs the refresh token lifetime in milliseconds
   */
  public JwtTokenOperation(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.access-token-expiration-ms}") long accessTokenExpirationMs,
      @Value("${jwt.refresh-token-expiration-ms}") long refreshTokenExpirationMs) {
    this.signingKey =
        Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.accessTokenExpirationMs = accessTokenExpirationMs;
    this.refreshTokenExpirationMs = refreshTokenExpirationMs;
  }

  @Override
  @Nonnull
  public String generateAccessToken(
      String userId, String username, List<Role> roles) {
    final var now = new Date();
    final var expiration = new Date(now.getTime() + accessTokenExpirationMs);
    final var rolesList = roles.stream().map(Role::name).toList();

    return Jwts.builder()
        .subject(userId)
        .claim(CLAIM_USERNAME, username)
        .claim(CLAIM_ROLES, rolesList)
        .claim(CLAIM_TYPE, TokenType.ACCESS.name())
        .issuedAt(now)
        .expiration(expiration)
        .signWith(signingKey)
        .compact();
  }

  @Override
  @Nonnull
  public String generateRefreshToken(String userId) {
    final var now = new Date();
    final var expiration = new Date(now.getTime() + refreshTokenExpirationMs);

    return Jwts.builder()
        .subject(userId)
        .claim(CLAIM_TYPE, TokenType.REFRESH.name())
        .issuedAt(now)
        .expiration(expiration)
        .signWith(signingKey)
        .compact();
  }

  @Override
  public boolean validateAccessToken(String token) {
    try {
      final var claims = parseClaims(token);
      final var type = claims.get(CLAIM_TYPE, String.class);
      return TokenType.ACCESS.name().equals(type) && !claims.getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  @Nonnull
  public String extractUserId(String token) {
    return parseClaims(token).getSubject();
  }

  @Override
  @Nonnull
  public String extractUsername(String token) {
    return parseClaims(token).get(CLAIM_USERNAME, String.class);
  }

  @Override
  @Nonnull
  @SuppressWarnings("unchecked")
  public List<Role> extractRoles(String token) {
    final List<String> rolesList =
        parseClaims(token).get(CLAIM_ROLES, List.class);
    if (rolesList == null) {
      return List.of();
    }
    return rolesList.stream()
        .map(Role::valueOf)
        .toList();
  }

  @Nonnull
  private Claims parseClaims(String token) {
    return Jwts.parser()
        .verifyWith(signingKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
