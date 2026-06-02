package edu.univalle.gadim.virtual_lab_platform.authentication.web.operation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.univalle.gadim.virtual_lab_platform.authentication.api.service.AuthenticationService;
import edu.univalle.gadim.virtual_lab_platform.authentication.api.service.AuthenticationService.AuthenticationResult;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.AuthenticatedUserResponse;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.LoginRequest;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.LoginResponse;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.LogoutRequest;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.RefreshTokenRequest;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.Role;
import edu.univalle.gadim.virtual_lab_platform.users.data.model.UserJpa;
import edu.univalle.gadim.virtual_lab_platform.users.data.model.UserRoleJpa;
import edu.univalle.gadim.virtual_lab_platform.users.data.repository.UserRepository;
import edu.univalle.gadim.virtual_lab_platform.users.data.repository.UserRoleRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@NullMarked
@DisplayName("AuthSpringWsOps")
class AuthSpringWsOpsUnTest {

  private static final String EMAIL = "ana.martinez@correounivalle.edu.co";
  private static final String NAME = "Ana";
  private static final String PASSWORD = "s3cur3p4ss";
  private static final String ACCESS_TOKEN = "access.jwt.token";
  private static final String REFRESH_TOKEN_VALUE = "refresh.jwt.token";

  private AuthenticationService authenticationService;
  private UserRepository userRepository;
  private UserRoleRepository userRoleRepository;
  private AuthSpringWsOps authSpringWsOps;

  @BeforeEach
  void setUp() {
    authenticationService = mock(AuthenticationService.class);
    userRepository = mock(UserRepository.class);
    userRoleRepository = mock(UserRoleRepository.class);
    authSpringWsOps =
        new AuthSpringWsOps(authenticationService, userRepository, userRoleRepository);
  }

  private AuthenticationResult buildAuthResult() {
    return new AuthenticationResult(ACCESS_TOKEN, REFRESH_TOKEN_VALUE, "Bearer", 900000);
  }

  private UserJpa buildUser() {
    return UserJpa.builder()
        .id(EMAIL)
        .name(NAME)
        .lastName("Martinez")
        .password("encoded")
        .status(edu.univalle.gadim.virtual_lab_platform.users.api.type.UserStatus.ACTIVE)
        .createdDate(LocalDateTime.of(2025, 1, 15, 10, 30, 0))
        .build();
  }

  @Nested
  @DisplayName("login")
  class Login {

    @Test
    @DisplayName("should delegate and map to LoginResponse")
    void shouldDelegateAndMap() {
      final var request = new LoginRequest(EMAIL, PASSWORD);
      final var authResult = buildAuthResult();

      when(authenticationService.login(EMAIL, PASSWORD)).thenReturn(authResult);

      final var result = authSpringWsOps.login(request);

      assertThat(result)
          .returns(ACCESS_TOKEN, LoginResponse::accessToken)
          .returns(REFRESH_TOKEN_VALUE, LoginResponse::refreshToken)
          .returns("Bearer", LoginResponse::tokenType)
          .returns(900000L, LoginResponse::expiresIn);
    }
  }

  @Nested
  @DisplayName("refresh")
  class Refresh {

    @Test
    @DisplayName("should delegate and map to LoginResponse")
    void shouldDelegateAndMap() {
      final var request = new RefreshTokenRequest(REFRESH_TOKEN_VALUE);
      final var authResult = buildAuthResult();

      when(authenticationService.refresh(REFRESH_TOKEN_VALUE)).thenReturn(authResult);

      final var result = authSpringWsOps.refresh(request);

      assertThat(result.accessToken()).isEqualTo(ACCESS_TOKEN);
    }
  }

  @Nested
  @DisplayName("logout")
  class Logout {

    @Test
    @DisplayName("should delegate to authentication service")
    void shouldDelegate() {
      final var request = new LogoutRequest(REFRESH_TOKEN_VALUE);

      authSpringWsOps.logout(request);

      verify(authenticationService).logout(REFRESH_TOKEN_VALUE);
    }
  }

  @Nested
  @DisplayName("me")
  class Me {

    @Test
    @DisplayName("should return authenticated user response")
    void shouldReturnAuthenticatedUser() {
      final var user = buildUser();
      final var userRole = UserRoleJpa.builder()
          .id("ur-001")
          .userId(EMAIL)
          .role(Role.STUDENT)
          .build();

      when(userRepository.findById(EMAIL)).thenReturn(Optional.of(user));
      when(userRoleRepository.findByUserId(EMAIL)).thenReturn(List.of(userRole));

      final var result = authSpringWsOps.me(EMAIL);

      assertThat(result)
          .returns(EMAIL, AuthenticatedUserResponse::id)
          .returns(NAME, AuthenticatedUserResponse::name)
          .returns("Martinez", AuthenticatedUserResponse::lastName);
      assertThat(result.roles()).containsExactly(Role.STUDENT);
    }

    @Test
    @DisplayName("should throw when user not found")
    void shouldThrowWhenUserNotFound() {
      when(userRepository.findById(EMAIL)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> authSpringWsOps.me(EMAIL))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("User not found");
    }
  }
}