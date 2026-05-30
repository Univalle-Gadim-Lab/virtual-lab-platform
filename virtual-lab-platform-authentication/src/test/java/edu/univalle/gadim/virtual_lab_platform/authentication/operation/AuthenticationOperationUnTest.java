package edu.univalle.gadim.virtual_lab_platform.authentication.operation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.univalle.gadim.virtual_lab_platform.authentication.api.service.AuthenticationService.AuthenticationResult;
import edu.univalle.gadim.virtual_lab_platform.authentication.api.service.TokenService;
import edu.univalle.gadim.virtual_lab_platform.authentication.data.model.RefreshTokenJpa;
import edu.univalle.gadim.virtual_lab_platform.authentication.data.repository.RefreshTokenRepository;
import edu.univalle.gadim.virtual_lab_platform.commons.type.UniqueIdGenerator;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.Role;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.UserStatus;
import edu.univalle.gadim.virtual_lab_platform.users.data.model.UserJpa;
import edu.univalle.gadim.virtual_lab_platform.users.data.model.UserRoleJpa;
import edu.univalle.gadim.virtual_lab_platform.users.data.repository.UserRepository;
import edu.univalle.gadim.virtual_lab_platform.users.data.repository.UserRoleRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

@NullMarked
@DisplayName("AuthenticationOperation")
class AuthenticationOperationUnTest {

  private static final String USER_ID = "user-001";
  private static final String USERNAME = "ana.martinez";
  private static final String PASSWORD = "s3cur3p4ss";
  private static final String ENCODED_PASSWORD = "$2a$encoded";
  private static final String ACCESS_TOKEN = "access.jwt.token";
  private static final String REFRESH_TOKEN_VALUE = "refresh.jwt.token";
  private static final String RT_ID = "rt-001";

  private UserRepository userRepository;
  private UserRoleRepository userRoleRepository;
  private PasswordEncoder passwordEncoder;
  private RefreshTokenRepository refreshTokenRepository;
  private UniqueIdGenerator idGenerator;
  private TokenService tokenService;
  private AuthenticationOperation authenticationOperation;

  @BeforeEach
  void setUp() {
    userRepository = mock(UserRepository.class);
    userRoleRepository = mock(UserRoleRepository.class);
    passwordEncoder = mock(PasswordEncoder.class);
    refreshTokenRepository = mock(RefreshTokenRepository.class);
    idGenerator = mock(UniqueIdGenerator.class);
    tokenService = mock(TokenService.class);
    authenticationOperation =
        new AuthenticationOperation(
            userRepository,
            userRoleRepository,
            passwordEncoder,
            refreshTokenRepository,
            idGenerator,
            tokenService);
  }

  private UserJpa buildActiveUser() {
    return UserJpa.builder()
        .id(USER_ID)
        .name(USERNAME)
        .lastName("Martinez")
        .password(ENCODED_PASSWORD)
        .status(UserStatus.ACTIVE)
        .createdDate(LocalDateTime.of(2025, 1, 15, 10, 30, 0))
        .build();
  }

  private UserRoleJpa buildUserRole() {
    return UserRoleJpa.builder()
        .id("ur-001")
        .userId(USER_ID)
        .role(Role.STUDENT)
        .build();
  }

  @Nested
  @DisplayName("login")
  class Login {

    @Test
    @DisplayName("should return tokens on successful login")
    void shouldReturnTokensOnSuccess() {
      final var user = buildActiveUser();
      final var userRole = buildUserRole();

      when(userRepository.findByName(USERNAME)).thenReturn(Optional.of(user));
      when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
      when(userRoleRepository.findByUserId(USER_ID)).thenReturn(List.of(userRole));
      when(tokenService.generateAccessToken(USER_ID, USERNAME, List.of(Role.STUDENT)))
          .thenReturn(ACCESS_TOKEN);
      when(tokenService.generateRefreshToken(USER_ID)).thenReturn(REFRESH_TOKEN_VALUE);
      when(idGenerator.generate()).thenReturn(RT_ID);

      final var result = authenticationOperation.login(USERNAME, PASSWORD);

      assertThat(result)
          .returns(ACCESS_TOKEN, AuthenticationResult::accessToken)
          .returns(REFRESH_TOKEN_VALUE, AuthenticationResult::refreshToken)
          .returns("Bearer", AuthenticationResult::tokenType);
      verify(refreshTokenRepository).save(any(RefreshTokenJpa.class));
    }

    @Test
    @DisplayName("should throw when user not found")
    void shouldThrowWhenUserNotFound() {
      when(userRepository.findByName(USERNAME)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> authenticationOperation.login(USERNAME, PASSWORD))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("User not found");
    }

    @Test
    @DisplayName("should throw when password is invalid")
    void shouldThrowWhenPasswordInvalid() {
      final var user = buildActiveUser();

      when(userRepository.findByName(USERNAME)).thenReturn(Optional.of(user));
      when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(false);

      assertThatThrownBy(() -> authenticationOperation.login(USERNAME, PASSWORD))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Invalid credentials");
    }

    @Test
    @DisplayName("should throw when user is not active")
    void shouldThrowWhenUserNotActive() {
      final var user = buildActiveUser();
      user.setStatus(UserStatus.INACTIVE);

      when(userRepository.findByName(USERNAME)).thenReturn(Optional.of(user));
      when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(true);

      assertThatThrownBy(() -> authenticationOperation.login(USERNAME, PASSWORD))
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("not active");
    }
  }

  @Nested
  @DisplayName("refresh")
  class Refresh {

    @Test
    @DisplayName("should return new access token on valid refresh")
    void shouldReturnNewAccessToken() {
      final var refreshToken = RefreshTokenJpa.builder()
          .id(RT_ID)
          .userId(USER_ID)
          .token(REFRESH_TOKEN_VALUE)
          .expiresAt(LocalDateTime.now().plusDays(7))
          .revoked(false)
          .createdAt(LocalDateTime.now())
          .build();
      final var user = buildActiveUser();
      final var userRole = buildUserRole();

      when(refreshTokenRepository.findByToken(REFRESH_TOKEN_VALUE))
          .thenReturn(Optional.of(refreshToken));
      when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
      when(userRoleRepository.findByUserId(USER_ID)).thenReturn(List.of(userRole));
      when(tokenService.generateAccessToken(USER_ID, USERNAME, List.of(Role.STUDENT)))
          .thenReturn(ACCESS_TOKEN);

      final var result = authenticationOperation.refresh(REFRESH_TOKEN_VALUE);

      assertThat(result.accessToken()).isEqualTo(ACCESS_TOKEN);
    }

    @Test
    @DisplayName("should throw when refresh token not found")
    void shouldThrowWhenTokenNotFound() {
      when(refreshTokenRepository.findByToken(REFRESH_TOKEN_VALUE)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> authenticationOperation.refresh(REFRESH_TOKEN_VALUE))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Refresh token not found");
    }

    @Test
    @DisplayName("should throw when refresh token is revoked")
    void shouldThrowWhenTokenRevoked() {
      final var refreshToken = RefreshTokenJpa.builder()
          .id(RT_ID)
          .userId(USER_ID)
          .token(REFRESH_TOKEN_VALUE)
          .expiresAt(LocalDateTime.now().plusDays(7))
          .revoked(true)
          .createdAt(LocalDateTime.now())
          .build();

      when(refreshTokenRepository.findByToken(REFRESH_TOKEN_VALUE))
          .thenReturn(Optional.of(refreshToken));

      assertThatThrownBy(() -> authenticationOperation.refresh(REFRESH_TOKEN_VALUE))
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("revoked");
    }

    @Test
    @DisplayName("should throw when refresh token is expired")
    void shouldThrowWhenTokenExpired() {
      final var refreshToken = RefreshTokenJpa.builder()
          .id(RT_ID)
          .userId(USER_ID)
          .token(REFRESH_TOKEN_VALUE)
          .expiresAt(LocalDateTime.now().minusDays(1))
          .revoked(false)
          .createdAt(LocalDateTime.now().minusDays(8))
          .build();

      when(refreshTokenRepository.findByToken(REFRESH_TOKEN_VALUE))
          .thenReturn(Optional.of(refreshToken));

      assertThatThrownBy(() -> authenticationOperation.refresh(REFRESH_TOKEN_VALUE))
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("expired");
    }
  }

  @Nested
  @DisplayName("logout")
  class Logout {

    @Test
    @DisplayName("should revoke refresh token")
    void shouldRevokeRefreshToken() {
      final var refreshToken = RefreshTokenJpa.builder()
          .id(RT_ID)
          .userId(USER_ID)
          .token(REFRESH_TOKEN_VALUE)
          .expiresAt(LocalDateTime.now().plusDays(7))
          .revoked(false)
          .createdAt(LocalDateTime.now())
          .build();

      when(refreshTokenRepository.findByToken(REFRESH_TOKEN_VALUE))
          .thenReturn(Optional.of(refreshToken));

      authenticationOperation.logout(REFRESH_TOKEN_VALUE);

      assertThat(refreshToken.isRevoked()).isTrue();
      verify(refreshTokenRepository).save(refreshToken);
    }

    @Test
    @DisplayName("should throw when refresh token not found")
    void shouldThrowWhenTokenNotFound() {
      when(refreshTokenRepository.findByToken(REFRESH_TOKEN_VALUE)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> authenticationOperation.logout(REFRESH_TOKEN_VALUE))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Refresh token not found");
    }
  }

  @Nested
  @DisplayName("validateAccessToken")
  class ValidateAccessToken {

    @Test
    @DisplayName("should delegate to token service")
    void shouldDelegateToTokenService() {
      when(tokenService.validateAccessToken(ACCESS_TOKEN)).thenReturn(true);

      assertThat(authenticationOperation.validateAccessToken(ACCESS_TOKEN)).isTrue();
      verify(tokenService).validateAccessToken(ACCESS_TOKEN);
    }
  }
}
