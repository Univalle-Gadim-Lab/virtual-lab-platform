package edu.univalle.gadim.virtual_lab_platform.users.web.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import edu.univalle.gadim.virtual_lab_platform.authentication.api.service.TokenService;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.security.JwtAuthenticationFilter;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.Role;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.CreateUserRoleRequest;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.UserRoleResponse;
import edu.univalle.gadim.virtual_lab_platform.users.web.ops.UsersWsOps;
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
@DisplayName("UserRoleController Role-Based Access Tests")
@ExtendWith(MockitoExtension.class)
class UserRoleControllerRoleInTest {

  private static final String USER_ID = "ana.martinez@correounivalle.edu.co";
  private static final String ROLE_ID = "role-001";
  private static final String ACCESS_TOKEN = "valid.access.token";

  @Mock private TokenService tokenService;
  @Mock private UsersWsOps usersWsOps;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    final var filter = new JwtAuthenticationFilter(tokenService);
    final var controller = new UserRoleController(usersWsOps);
    mockMvc = standaloneSetup(controller).addFilter(filter).build();
  }

  private void mockValidToken(String userId, List<Role> roles) {
    when(tokenService.validateAccessToken(ACCESS_TOKEN)).thenReturn(true);
    when(tokenService.extractUserId(ACCESS_TOKEN)).thenReturn(userId);
    when(tokenService.extractRoles(ACCESS_TOKEN)).thenReturn(roles);
  }

  @Test
  @DisplayName("should allow createUserRole with ADMIN token")
  void shouldAllowCreateUserRoleWithAdminToken() throws Exception {
    mockValidToken(USER_ID, List.of(Role.ADMIN));

    final var response = new UserRoleResponse(ROLE_ID, USER_ID, Role.STUDENT);
    when(usersWsOps.createUserRole(ArgumentMatchers.any(CreateUserRoleRequest.class)))
        .thenReturn(response);

    mockMvc.perform(
            post("/api/user-roles")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":\"" + USER_ID + "\",\"role\":\"STUDENT\"}"))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("should allow getRolesByUserId with ADMIN token")
  void shouldAllowGetRolesByUserIdWithAdminToken() throws Exception {
    mockValidToken(USER_ID, List.of(Role.ADMIN));

    final var response = new UserRoleResponse(ROLE_ID, USER_ID, Role.STUDENT);
    when(usersWsOps.getRolesByUserId(USER_ID)).thenReturn(List.of(response));

    mockMvc.perform(
            get("/api/user-roles")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .param("userId", USER_ID))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("should allow deleteUserRole with ADMIN token")
  void shouldAllowDeleteUserRoleWithAdminToken() throws Exception {
    mockValidToken(USER_ID, List.of(Role.ADMIN));

    mockMvc.perform(
            delete("/api/user-roles/" + ROLE_ID)
                .header("Authorization", "Bearer " + ACCESS_TOKEN))
        .andExpect(status().isNoContent());
  }
}
