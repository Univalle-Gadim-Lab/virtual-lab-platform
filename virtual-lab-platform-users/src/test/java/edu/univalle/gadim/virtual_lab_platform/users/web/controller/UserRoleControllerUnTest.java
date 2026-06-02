package edu.univalle.gadim.virtual_lab_platform.users.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.univalle.gadim.virtual_lab_platform.users.api.type.Role;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.CreateUserRolesRequest;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.CreateUserRoleRequest;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.UserRoleResponse;
import edu.univalle.gadim.virtual_lab_platform.users.web.ops.UsersWsOps;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@NullMarked
@DisplayName("UserRoleController")
class UserRoleControllerUnTest {

  private static final String USER_ID = "ana.martinez@correounivalle.edu.co";
  private static final String ROLE_ID = "role-001";

  private UsersWsOps usersWsOps;
  private UserRoleController controller;

  @BeforeEach
  void setUp() {
    usersWsOps = mock(UsersWsOps.class);
    controller = new UserRoleController(usersWsOps);
  }

  private UserRoleResponse buildRoleResponse() {
    return new UserRoleResponse(ROLE_ID, USER_ID, Role.STUDENT);
  }

  @Nested
  @DisplayName("createUserRole")
  class CreateUserRole {

    @Test
    @DisplayName("should return 200 with created role")
    void shouldReturn200WithCreatedRole() {
      // Given
      final var request = new CreateUserRoleRequest(USER_ID, Role.STUDENT);
      final var response = buildRoleResponse();
      when(usersWsOps.createUserRole(request)).thenReturn(response);

      // When
      final var result = controller.createUserRole(request);

      // Then
      assertThat(result.getStatusCode().value()).isEqualTo(200);
      assertThat(result.getBody()).isEqualTo(response);
    }
  }

  @Nested
  @DisplayName("createUserRoles")
  class CreateUserRoles {

    @Test
    @DisplayName("should return 200 with created roles")
    void shouldReturn200WithCreatedRoles() {
      // Given
      final var request = new CreateUserRolesRequest(USER_ID, List.of(Role.STUDENT, Role.TEACHER));
      final var response1 = buildRoleResponse();
      final var response2 = new UserRoleResponse("role-002", USER_ID, Role.TEACHER);
      when(usersWsOps.createUserRoles(request)).thenReturn(List.of(response1, response2));

      // When
      final var result = controller.createUserRoles(request);

      // Then
      assertThat(result.getStatusCode().value()).isEqualTo(200);
      assertThat(result.getBody()).hasSize(2);
    }
  }

  @Nested
  @DisplayName("getRolesByUserId")
  class GetRolesByUserId {

    @Test
    @DisplayName("should return 200 with list of roles")
    void shouldReturn200WithListOfRoles() {
      // Given
      final var response = buildRoleResponse();
      when(usersWsOps.getRolesByUserId(USER_ID)).thenReturn(List.of(response));

      // When
      final var result = controller.getRolesByUserId(USER_ID);

      // Then
      assertThat(result.getStatusCode().value()).isEqualTo(200);
      assertThat(result.getBody()).hasSize(1);
    }
  }

  @Nested
  @DisplayName("deleteUserRole")
  class DeleteUserRole {

    @Test
    @DisplayName("should return 204 on successful deletion")
    void shouldReturn204OnSuccess() {
      // Given
      doNothing().when(usersWsOps).deleteUserRole(ROLE_ID);

      // When
      final var result = controller.deleteUserRole(ROLE_ID);

      // Then
      assertThat(result.getStatusCode().value()).isEqualTo(204);
      verify(usersWsOps).deleteUserRole(ROLE_ID);
    }

    @Test
    @DisplayName("should return 404 when role not found")
    void shouldReturn404WhenNotFound() {
      // Given
      doThrow(new IllegalArgumentException("not found")).when(usersWsOps).deleteUserRole("nonexistent");

      // When
      final var result = controller.deleteUserRole("nonexistent");

      // Then
      assertThat(result.getStatusCode().value()).isEqualTo(404);
    }
  }
}
