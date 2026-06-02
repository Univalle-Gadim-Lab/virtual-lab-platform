package edu.univalle.gadim.virtual_lab_platform.authentication.web.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import edu.univalle.gadim.virtual_lab_platform.authentication.api.service.TokenService;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.AuthenticatedUserResponse;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.LoginRequest;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.LoginResponse;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.RefreshTokenRequest;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.ops.AuthWsOps;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.security.JwtAuthenticationFilter;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.Role;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@NullMarked
@DisplayName("AuthController Integration Tests")
@ExtendWith(MockitoExtension.class)
class AuthControllerInTest {

  private static final String USER_ID = "ana.martinez@correounivalle.edu.co";
  private static final String EMAIL = "ana.martinez@correounivalle.edu.co";
  private static final String PASSWORD = "s3cur3p4ss";
  private static final String ACCESS_TOKEN = "valid.access.token";
  private static final String INVALID_TOKEN = "invalid.token.value";
  private static final String REFRESH_TOKEN_VALUE = "valid.refresh.token";

  @Mock private TokenService tokenService;
  @Mock private AuthWsOps authWsOps;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    final var filter = new JwtAuthenticationFilter(tokenService);
    final var controller = new AuthController(authWsOps);
    mockMvc = standaloneSetup(controller).addFilter(filter).build();
  }

  @Test
  @DisplayName("should allow login without authentication")
  void shouldAllowLoginWithoutAuthentication() throws Exception {
    final var loginResponse =
        new LoginResponse(ACCESS_TOKEN, REFRESH_TOKEN_VALUE, "Bearer", 900000);
    when(authWsOps.login(ArgumentMatchers.any(LoginRequest.class))).thenReturn(loginResponse);

    mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"email\":\"" + EMAIL + "\",\"password\":\"" + PASSWORD + "\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.access_token").value(ACCESS_TOKEN))
        .andExpect(jsonPath("$.refresh_token").value(REFRESH_TOKEN_VALUE));
  }

  @Test
  @DisplayName("should allow refresh without authentication")
  void shouldAllowRefreshWithoutAuthentication() throws Exception {
    final var loginResponse =
        new LoginResponse(ACCESS_TOKEN, REFRESH_TOKEN_VALUE, "Bearer", 900000);
    when(authWsOps.refresh(ArgumentMatchers.any(RefreshTokenRequest.class)))
        .thenReturn(loginResponse);

    mockMvc.perform(
            post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"refresh_token\":\"" + REFRESH_TOKEN_VALUE + "\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.access_token").value(ACCESS_TOKEN));
  }

  @Test
  @DisplayName("should skip JWT filter for login endpoint")
  void shouldSkipJwtFilterForLoginEndpoint() throws Exception {
    final var loginResponse =
        new LoginResponse(ACCESS_TOKEN, REFRESH_TOKEN_VALUE, "Bearer", 900000);
    when(authWsOps.login(ArgumentMatchers.any(LoginRequest.class))).thenReturn(loginResponse);

    mockMvc.perform(
            post("/api/auth/login")
                .header("Authorization", "Bearer invalid.token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"email\":\"" + EMAIL + "\",\"password\":\"" + PASSWORD + "\"}"))
        .andExpect(status().isOk());

    verify(tokenService, org.mockito.Mockito.never())
        .validateAccessToken(ArgumentMatchers.anyString());
  }

  @Test
  @DisplayName("should skip JWT filter for refresh endpoint")
  void shouldSkipJwtFilterForRefreshEndpoint() throws Exception {
    final var loginResponse =
        new LoginResponse(ACCESS_TOKEN, REFRESH_TOKEN_VALUE, "Bearer", 900000);
    when(authWsOps.refresh(ArgumentMatchers.any(RefreshTokenRequest.class)))
        .thenReturn(loginResponse);

    mockMvc.perform(
            post("/api/auth/refresh")
                .header("Authorization", "Bearer invalid.token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"refresh_token\":\"" + REFRESH_TOKEN_VALUE + "\"}"))
        .andExpect(status().isOk());

    verify(tokenService, org.mockito.Mockito.never())
        .validateAccessToken(ArgumentMatchers.anyString());
  }

  @Test
  @DisplayName("should populate security context with valid token")
  void shouldPopulateSecurityContextWithValidToken() throws Exception {
    when(tokenService.validateAccessToken(ACCESS_TOKEN)).thenReturn(true);
    when(tokenService.extractUserId(ACCESS_TOKEN)).thenReturn(USER_ID);
    when(tokenService.extractRoles(ACCESS_TOKEN)).thenReturn(List.of(Role.STUDENT));

    final var userResponse =
        new AuthenticatedUserResponse(USER_ID, "Ana", "Martinez", Set.of(Role.STUDENT));
    when(authWsOps.me(USER_ID)).thenReturn(userResponse);

    mockMvc.perform(get("/api/auth/me").header("Authorization", "Bearer " + ACCESS_TOKEN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(USER_ID))
        .andExpect(jsonPath("$.name").value("Ana"))
        .andExpect(jsonPath("$.lastName").value("Martinez"));
  }

  @Test
  @DisplayName("should not set security context with invalid token")
  void shouldNotSetSecurityContextWithInvalidToken() throws Exception {
    when(tokenService.validateAccessToken(INVALID_TOKEN)).thenReturn(false);

    mockMvc.perform(get("/api/auth/me").header("Authorization", "Bearer " + INVALID_TOKEN));

    verify(tokenService).validateAccessToken(INVALID_TOKEN);
    verify(tokenService, org.mockito.Mockito.never()).extractUserId(INVALID_TOKEN);
  }

  @Test
  @DisplayName("should not set security context without authorization header")
  void shouldNotSetSecurityContextWithoutAuthorizationHeader() throws Exception {
    mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"email\":\"" + EMAIL + "\",\"password\":\"" + PASSWORD + "\"}"))
        .andExpect(status().isOk());

    verify(tokenService, org.mockito.Mockito.never())
        .validateAccessToken(ArgumentMatchers.anyString());
  }

  @Test
  @DisplayName("should propagate token validation to token service")
  void shouldPropagateTokenValidationToTokenService() throws Exception {
    when(tokenService.validateAccessToken(ACCESS_TOKEN)).thenReturn(true);
    when(tokenService.extractUserId(ACCESS_TOKEN)).thenReturn(USER_ID);
    when(tokenService.extractRoles(ACCESS_TOKEN)).thenReturn(List.of(Role.STUDENT));

    final var userResponse =
        new AuthenticatedUserResponse(USER_ID, "Ana", "Martinez", Set.of(Role.STUDENT));
    when(authWsOps.me(USER_ID)).thenReturn(userResponse);

    mockMvc.perform(get("/api/auth/me").header("Authorization", "Bearer " + ACCESS_TOKEN))
        .andExpect(status().isOk());

    verify(tokenService).validateAccessToken(ACCESS_TOKEN);
    verify(tokenService).extractUserId(ACCESS_TOKEN);
    verify(tokenService).extractRoles(ACCESS_TOKEN);
  }

  @Test
  @DisplayName("should allow logout with valid token")
  void shouldAllowLogoutWithValidToken() throws Exception {
    when(tokenService.validateAccessToken(ACCESS_TOKEN)).thenReturn(true);
    when(tokenService.extractUserId(ACCESS_TOKEN)).thenReturn(USER_ID);
    when(tokenService.extractRoles(ACCESS_TOKEN)).thenReturn(List.of(Role.STUDENT));

    mockMvc.perform(
            post("/api/auth/logout")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"refresh_token\":\"" + REFRESH_TOKEN_VALUE + "\"}"))
        .andExpect(status().isNoContent());
  }
}
