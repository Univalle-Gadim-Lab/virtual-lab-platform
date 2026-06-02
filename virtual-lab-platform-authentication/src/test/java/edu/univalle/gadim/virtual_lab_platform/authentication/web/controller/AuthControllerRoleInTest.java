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
import org.springframework.test.web.servlet.MockMvc;

@NullMarked
@DisplayName("AuthController Role-Based Access Tests")
@ExtendWith(MockitoExtension.class)
class AuthControllerRoleInTest {

  private static final String USER_ID = "ana.martinez@correounivalle.edu.co";
  private static final String ACCESS_TOKEN = "valid.access.token";
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

  private void mockValidToken(String userId, List<Role> roles) {
    when(tokenService.validateAccessToken(ACCESS_TOKEN)).thenReturn(true);
    when(tokenService.extractUserId(ACCESS_TOKEN)).thenReturn(userId);
    when(tokenService.extractRoles(ACCESS_TOKEN)).thenReturn(roles);
  }

  @Test
  @DisplayName("should allow /api/auth/me with ADMIN token")
  void shouldAllowMeWithAdminToken() throws Exception {
    mockValidToken(USER_ID, List.of(Role.ADMIN));

    final var userResponse =
        new AuthenticatedUserResponse(USER_ID, "Admin", "User", Set.of(Role.ADMIN));
    when(authWsOps.me(USER_ID)).thenReturn(userResponse);

    mockMvc.perform(get("/api/auth/me").header("Authorization", "Bearer " + ACCESS_TOKEN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(USER_ID))
        .andExpect(jsonPath("$.roles[0]").value("ADMIN"));
  }

  @Test
  @DisplayName("should allow /api/auth/me with TEACHER token")
  void shouldAllowMeWithTeacherToken() throws Exception {
    mockValidToken(USER_ID, List.of(Role.TEACHER));

    final var userResponse =
        new AuthenticatedUserResponse(USER_ID, "Teacher", "User", Set.of(Role.TEACHER));
    when(authWsOps.me(USER_ID)).thenReturn(userResponse);

    mockMvc.perform(get("/api/auth/me").header("Authorization", "Bearer " + ACCESS_TOKEN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(USER_ID))
        .andExpect(jsonPath("$.roles[0]").value("TEACHER"));
  }

  @Test
  @DisplayName("should allow /api/auth/me with STUDENT token")
  void shouldAllowMeWithStudentToken() throws Exception {
    mockValidToken(USER_ID, List.of(Role.STUDENT));

    final var userResponse =
        new AuthenticatedUserResponse(USER_ID, "Student", "User", Set.of(Role.STUDENT));
    when(authWsOps.me(USER_ID)).thenReturn(userResponse);

    mockMvc.perform(get("/api/auth/me").header("Authorization", "Bearer " + ACCESS_TOKEN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(USER_ID))
        .andExpect(jsonPath("$.roles[0]").value("STUDENT"));
  }

  @Test
  @DisplayName("should allow /api/auth/logout with ADMIN token")
  void shouldAllowLogoutWithAdminToken() throws Exception {
    mockValidToken(USER_ID, List.of(Role.ADMIN));

    mockMvc.perform(
            post("/api/auth/logout")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .contentType("application/json")
                .content("{\"refresh_token\":\"" + REFRESH_TOKEN_VALUE + "\"}"))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("should allow /api/auth/logout with TEACHER token")
  void shouldAllowLogoutWithTeacherToken() throws Exception {
    mockValidToken(USER_ID, List.of(Role.TEACHER));

    mockMvc.perform(
            post("/api/auth/logout")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .contentType("application/json")
                .content("{\"refresh_token\":\"" + REFRESH_TOKEN_VALUE + "\"}"))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("should allow /api/auth/logout with STUDENT token")
  void shouldAllowLogoutWithStudentToken() throws Exception {
    mockValidToken(USER_ID, List.of(Role.STUDENT));

    mockMvc.perform(
            post("/api/auth/logout")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .contentType("application/json")
                .content("{\"refresh_token\":\"" + REFRESH_TOKEN_VALUE + "\"}"))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("should not set security context without token")
  void shouldNotSetSecurityContextWithoutToken() throws Exception {
    mockMvc.perform(
            post("/api/auth/login")
                .contentType("application/json")
                .content("{\"email\":\"user\",\"password\":\"pass\"}"))
        .andExpect(status().isOk());

    verify(tokenService, org.mockito.Mockito.never())
        .validateAccessToken(ArgumentMatchers.anyString());
  }
}
