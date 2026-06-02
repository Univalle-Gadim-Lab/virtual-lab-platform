package edu.univalle.gadim.virtual_lab_platform.authentication.operation;

import edu.univalle.gadim.virtual_lab_platform.authentication.api.service.AuthenticationService;
import edu.univalle.gadim.virtual_lab_platform.authentication.api.service.TokenService;
import edu.univalle.gadim.virtual_lab_platform.authentication.data.model.RefreshTokenJpa;
import edu.univalle.gadim.virtual_lab_platform.authentication.data.repository.RefreshTokenRepository;
import edu.univalle.gadim.virtual_lab_platform.commons.type.UniqueIdGenerator;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.Role;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.UserStatus;
import edu.univalle.gadim.virtual_lab_platform.users.data.repository.UserRepository;
import edu.univalle.gadim.virtual_lab_platform.users.data.repository.UserRoleRepository;
import java.time.LocalDateTime;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link AuthenticationService} that verifies credentials
 * against persisted user data and manages refresh token lifecycle.
 *
 * <p>Delegates JWT generation and validation to {@link TokenService} and persists
 * refresh tokens so they can be explicitly revoked on logout.
 *
 * @see AuthenticationService
 * @see TokenService
 * @see RefreshTokenRepository
 */
@Service
@ParametersAreNonnullByDefault
public class AuthenticationOperation implements AuthenticationService {

  private final UserRepository userRepository;
  private final UserRoleRepository userRoleRepository;
  private final PasswordEncoder passwordEncoder;
  private final RefreshTokenRepository refreshTokenRepository;
  private final UniqueIdGenerator idGenerator;
  private final TokenService tokenService;

  /**
   * Constructs a new {@code AuthenticationOperation} with the required dependencies.
   *
   * @param userRepository          the user repository for credential lookup
   * @param userRoleRepository      the user role repository for loading authorization roles
   * @param passwordEncoder         the password encoder for credential verification
   * @param refreshTokenRepository  the refresh token repository for token persistence
   * @param idGenerator             the unique ID generator for refresh token identifiers
   * @param tokenService            the token service for JWT generation and validation
   */
  public AuthenticationOperation(
      UserRepository userRepository,
      UserRoleRepository userRoleRepository,
      PasswordEncoder passwordEncoder,
      RefreshTokenRepository refreshTokenRepository,
      UniqueIdGenerator idGenerator,
      TokenService tokenService) {
    this.userRepository = userRepository;
    this.userRoleRepository = userRoleRepository;
    this.passwordEncoder = passwordEncoder;
    this.refreshTokenRepository = refreshTokenRepository;
    this.idGenerator = idGenerator;
    this.tokenService = tokenService;
  }

  @Override
  @Nonnull
  public AuthenticationResult login(String email, String password) {
    final var user =
        userRepository
            .findById(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));

    if (!passwordEncoder.matches(password, user.password())) {
      throw new IllegalArgumentException("Invalid credentials");
    }

    if (user.status() != UserStatus.ACTIVE) {
      throw new IllegalStateException("User account is not active: " + user.status());
    }

    final var roles =
        userRoleRepository.findByUserId(user.id()).stream()
            .map(ur -> Role.valueOf(ur.role().name()))
            .toList();

    final var accessToken =
        tokenService.generateAccessToken(user.id(), user.name(), roles);
    final var refreshTokenValue = tokenService.generateRefreshToken(user.id());

    final var refreshToken = RefreshTokenJpa.builder()
        .id(idGenerator.generate())
        .userId(user.id())
        .token(refreshTokenValue)
        .expiresAt(LocalDateTime.now().plusSeconds(604800))
        .revoked(false)
        .createdAt(LocalDateTime.now())
        .build();
    refreshTokenRepository.save(refreshToken);

    return new AuthenticationResult(accessToken, refreshTokenValue, "Bearer", 900000);
  }

  @Override
  @Nonnull
  public AuthenticationResult refresh(String refreshTokenValue) {
    final var refreshToken =
        refreshTokenRepository
            .findByToken(refreshTokenValue)
            .orElseThrow(
                () -> new IllegalArgumentException("Refresh token not found"));

    if (refreshToken.revoked()) {
      throw new IllegalStateException("Refresh token has been revoked");
    }

    if (refreshToken.expiresAt().isBefore(LocalDateTime.now())) {
      throw new IllegalStateException("Refresh token has expired");
    }

    final var user =
        userRepository
            .findById(refreshToken.userId())
            .orElseThrow(
                () -> new IllegalArgumentException(
                    "User not found: " + refreshToken.userId()));

    final var roles =
        userRoleRepository.findByUserId(user.id()).stream()
            .map(ur -> Role.valueOf(ur.role().name()))
            .toList();

    final var accessToken =
        tokenService.generateAccessToken(user.id(), user.name(), roles);

    return new AuthenticationResult(accessToken, refreshTokenValue, "Bearer", 900000);
  }

  @Override
  public void logout(String refreshTokenValue) {
    final var refreshToken =
        refreshTokenRepository
            .findByToken(refreshTokenValue)
            .orElseThrow(
                () -> new IllegalArgumentException("Refresh token not found"));

    refreshToken.setRevoked(true);
    refreshTokenRepository.save(refreshToken);
  }

  @Override
  public boolean validateAccessToken(String accessToken) {
    return tokenService.validateAccessToken(accessToken);
  }
}
