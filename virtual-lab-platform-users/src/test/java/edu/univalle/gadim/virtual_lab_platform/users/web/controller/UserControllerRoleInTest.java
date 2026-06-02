package edu.univalle.gadim.virtual_lab_platform.users.web.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import edu.univalle.gadim.virtual_lab_platform.authentication.api.service.TokenService;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.security.JwtAuthenticationFilter;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.Role;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.UserStatus;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.CreateUserRequest;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.UserResponse;
import edu.univalle.gadim.virtual_lab_platform.users.web.ops.UsersWsOps;
import java.time.LocalDateTime;
import java.util.List;
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
@DisplayName("UserController Role-Based Access Tests")
@ExtendWith(MockitoExtension.class)
class UserControllerRoleInTest {

  private static final String USER_ID = "ana.martinez@correounivalle.edu.co";
  private static final String ACCESS_TOKEN = "valid.access.token";

  @Mock private TokenService tokenService;
  @Mock private UsersWsOps usersWsOps;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    final var filter = new JwtAuthenticationFilter(tokenService);
    final var controller = new UserController(usersWsOps);
    mockMvc = standaloneSetup(controller).addFilter(filter).build();
  }

  private void mockValidToken(String userId, List<Role> roles) {
    when(tokenService.validateAccessToken(ACCESS_TOKEN)).thenReturn(true);
    when(tokenService.extractUserId(ACCESS_TOKEN)).thenReturn(userId);
    when(tokenService.extractRoles(ACCESS_TOKEN)).thenReturn(roles);
  }

  private UserResponse buildUserResponse() {
    return new UserResponse(
        USER_ID, "Ana", "Martinez", "EXT-001", UserStatus.ACTIVE,
        LocalDateTime.of(2025, 1, 15, 10, 30, 0));
  }

  @Test
  @DisplayName("should allow createUser with ADMIN token")
  void shouldAllowCreateUserWithAdminToken() throws Exception {
    mockValidToken(USER_ID, List.of(Role.ADMIN));

    final var request =
        new CreateUserRequest(USER_ID, "Ana", "Martinez", null, "pass123", UserStatus.ACTIVE);
    when(usersWsOps.createUser(ArgumentMatchers.any(CreateUserRequest.class)))
        .thenReturn(buildUserResponse());

    mockMvc.perform(
            post("/api/users")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"" + USER_ID + "\",\"name\":\"Ana\",\"lastName\":\"Martinez\","
                    + "\"password\":\"pass123\",\"status\":\"ACTIVE\"}"))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("should allow getAllUsers with ADMIN token")
  void shouldAllowGetAllUsersWithAdminToken() throws Exception {
    mockValidToken(USER_ID, List.of(Role.ADMIN));

    when(usersWsOps.getAllUsers()).thenReturn(List.of(buildUserResponse()));

    mockMvc.perform(get("/api/users").header("Authorization", "Bearer " + ACCESS_TOKEN))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("should allow getUser with ADMIN token")
  void shouldAllowGetUserWithAdminToken() throws Exception {
    mockValidToken(USER_ID, List.of(Role.ADMIN));

    when(usersWsOps.getUserById(USER_ID)).thenReturn(buildUserResponse());

    mockMvc.perform(get("/api/users/" + USER_ID).header("Authorization", "Bearer " + ACCESS_TOKEN))
        .andExpect(status().isOk());
  }
}
