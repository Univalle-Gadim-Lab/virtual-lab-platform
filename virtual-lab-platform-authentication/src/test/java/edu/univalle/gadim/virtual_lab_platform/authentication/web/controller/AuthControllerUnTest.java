package edu.univalle.gadim.virtual_lab_platform.authentication.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.AuthenticatedUserResponse;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.LoginRequest;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.LoginResponse;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.LogoutRequest;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.RefreshTokenRequest;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.ops.AuthWsOps;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.Role;
import java.util.EnumSet;
import java.util.Set;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@NullMarked
@DisplayName("AuthController")
class AuthControllerUnTest {

  private static final String ACCESS_TOKEN = "access.jwt.token";
  private static final String REFRESH_TOKEN_VALUE = "refresh.jwt.token";
  private static final String USER_ID = "ana.martinez@correounivalle.edu.co";

  private AuthWsOps authWsOps;
  private AuthController controller;

  @BeforeEach
  void setUp() {
    authWsOps = mock(AuthWsOps.class);
    controller = new AuthController(authWsOps);
  }

  private LoginResponse buildLoginResponse() {
    return new LoginResponse(ACCESS_TOKEN, REFRESH_TOKEN_VALUE, "Bearer", 900000);
  }

  @Nested
  @DisplayName("login")
  class Login {

    @Test
    @DisplayName("should return 200 with tokens")
    void shouldReturn200WithTokens() {
      final var request = new LoginRequest("ana.martinez@correounivalle.edu.co", "s3cur3p4ss");
      final var response = buildLoginResponse();
      when(authWsOps.login(request)).thenReturn(response);

      final var result = controller.login(request);

      assertThat(result.getStatusCode().value()).isEqualTo(200);
      assertThat(result.getBody()).isEqualTo(response);
    }
  }

  @Nested
  @DisplayName("refresh")
  class Refresh {

    @Test
    @DisplayName("should return 200 with new tokens")
    void shouldReturn200WithNewTokens() {
      final var request = new RefreshTokenRequest(REFRESH_TOKEN_VALUE);
      final var response = buildLoginResponse();
      when(authWsOps.refresh(request)).thenReturn(response);

      final var result = controller.refresh(request);

      assertThat(result.getStatusCode().value()).isEqualTo(200);
      assertThat(result.getBody()).isEqualTo(response);
    }
  }

  @Nested
  @DisplayName("logout")
  class Logout {

    @Test
    @DisplayName("should return 204 on successful logout")
    void shouldReturn204OnSuccess() {
      final var request = new LogoutRequest(REFRESH_TOKEN_VALUE);

      final var result = controller.logout(request);

      assertThat(result.getStatusCode().value()).isEqualTo(204);
      verify(authWsOps).logout(request);
    }
  }

  @Nested
  @DisplayName("me")
  class Me {

    @Test
    @DisplayName("should return 200 with authenticated user")
    void shouldReturn200WithAuthenticatedUser() {
      final var response =
          new AuthenticatedUserResponse(USER_ID, "Ana", "Martinez", Set.of(Role.STUDENT));

      final var authentication = mock(Authentication.class);
      when(authentication.getName()).thenReturn(USER_ID);

      final var securityContext = mock(SecurityContext.class);
      when(securityContext.getAuthentication()).thenReturn(authentication);
      SecurityContextHolder.setContext(securityContext);

      when(authWsOps.me(USER_ID)).thenReturn(response);

      final var result = controller.me();

      assertThat(result.getStatusCode().value()).isEqualTo(200);
      assertThat(result.getBody()).isEqualTo(response);

      SecurityContextHolder.clearContext();
    }
  }
}
