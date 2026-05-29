package edu.univalle.gadim.virtual_lab_platform.users.web.operation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.univalle.gadim.virtual_lab_platform.users.api.service.UserRoleService;
import edu.univalle.gadim.virtual_lab_platform.users.api.service.UserService;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.Role;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.User;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.UserStatus;
import edu.univalle.gadim.virtual_lab_platform.users.data.model.UserJpa;
import edu.univalle.gadim.virtual_lab_platform.users.data.model.UserRoleJpa;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.CreateUserRequest;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.CreateUserRolesRequest;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.CreateUserRoleRequest;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.UpdateUserRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@NullMarked
@DisplayName("UsersSpringWsOps")
class UsersSpringWsOpsUnTest {

  private static final String USER_ID = "user-001";
  private static final String ROLE_ID = "role-001";

  private UserService userService;
  private UserRoleService userRoleService;
  private UsersSpringWsOps wsOps;

  @BeforeEach
  void setUp() {
    userService = mock(UserService.class);
    userRoleService = mock(UserRoleService.class);
    wsOps = new UsersSpringWsOps(userService, userRoleService);
  }

  private UserJpa buildUser() {
    return UserJpa.builder()
        .id(USER_ID)
        .name("Ana")
        .lastName("Martinez")
        .externalCode("EXT-001")
        .password("encoded")
        .status(UserStatus.ACTIVE)
        .createdDate(LocalDateTime.of(2025, 1, 15, 10, 30, 0))
        .build();
  }

  private UserRoleJpa buildUserRole() {
    return UserRoleJpa.builder().id(ROLE_ID).userId(USER_ID).role(Role.STUDENT).build();
  }

  @Nested
  @DisplayName("createUser")
  class CreateUser {

    @Test
    @DisplayName("should delegate to userService and map response")
    void shouldDelegateAndMapResponse() {
      // Given
      final var request = new CreateUserRequest("Ana", "Martinez", null, "pass123", UserStatus.ACTIVE);
      final var savedUser = buildUser();

      when(userService.createUser(any(User.class))).thenReturn(savedUser);

      // When
      final var result = wsOps.createUser(request);

      // Then
      assertThat(result)
          .returns(USER_ID, edu.univalle.gadim.virtual_lab_platform.users.web.model.UserResponse::id)
          .returns("Ana", edu.univalle.gadim.virtual_lab_platform.users.web.model.UserResponse::name)
          .returns("Martinez", edu.univalle.gadim.virtual_lab_platform.users.web.model.UserResponse::lastName);
      verify(userService).createUser(any(User.class));
    }
  }

  @Nested
  @DisplayName("getUserById")
  class GetUserById {

    @Test
    @DisplayName("should return user response when found")
    void shouldReturnUserResponseWhenFound() {
      // Given
      final var user = buildUser();
      when(userService.getUserById(USER_ID)).thenReturn(Optional.of(user));

      // When
      final var result = wsOps.getUserById(USER_ID);

      // Then
      assertThat(result)
          .returns(USER_ID, edu.univalle.gadim.virtual_lab_platform.users.web.model.UserResponse::id)
          .returns("Ana", edu.univalle.gadim.virtual_lab_platform.users.web.model.UserResponse::name);
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when not found")
    void shouldThrowWhenNotFound() {
      // Given
      when(userService.getUserById(anyString())).thenReturn(Optional.empty());

      // When / Then
      assertThatThrownBy(() -> wsOps.getUserById("nonexistent"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("nonexistent");
    }
  }

  @Nested
  @DisplayName("getAllUsers")
  class GetAllUsers {

    @Test
    @DisplayName("should return list of user responses")
    void shouldReturnListOfUserResponses() {
      // Given
      final var user = buildUser();
      when(userService.getAllUsers()).thenReturn(List.of(user));

      // When
      final var result = wsOps.getAllUsers();

      // Then
      assertThat(result).hasSize(1);
      assertThat(result.get(0)).returns(USER_ID, edu.univalle.gadim.virtual_lab_platform.users.web.model.UserResponse::id);
    }
  }

  @Nested
  @DisplayName("getUserByUsername")
  class GetUserByUsername {

    @Test
    @DisplayName("should return user response when found")
    void shouldReturnUserResponseWhenFound() {
      // Given
      final var user = buildUser();
      when(userService.getUserByUsername("Ana")).thenReturn(Optional.of(user));

      // When
      final var result = wsOps.getUserByUsername("Ana");

      // Then
      assertThat(result)
          .returns(USER_ID, edu.univalle.gadim.virtual_lab_platform.users.web.model.UserResponse::id)
          .returns("Ana", edu.univalle.gadim.virtual_lab_platform.users.web.model.UserResponse::name);
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when not found")
    void shouldThrowWhenNotFound() {
      // Given
      when(userService.getUserByUsername(anyString())).thenReturn(Optional.empty());

      // When / Then
      assertThatThrownBy(() -> wsOps.getUserByUsername("nonexistent"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("nonexistent");
    }
  }

  @Nested
  @DisplayName("createUserRole")
  class CreateUserRole {

    @Test
    @DisplayName("should delegate to userRoleService and map response")
    void shouldDelegateAndMapResponse() {
      // Given
      final var request = new CreateUserRoleRequest(USER_ID, Role.STUDENT);
      final var userRole = buildUserRole();

      when(userRoleService.createUserRole(USER_ID, Role.STUDENT)).thenReturn(userRole);

      // When
      final var result = wsOps.createUserRole(request);

      // Then
      assertThat(result)
          .returns(ROLE_ID, edu.univalle.gadim.virtual_lab_platform.users.web.model.UserRoleResponse::id)
          .returns(USER_ID, edu.univalle.gadim.virtual_lab_platform.users.web.model.UserRoleResponse::userId)
          .returns(Role.STUDENT, edu.univalle.gadim.virtual_lab_platform.users.web.model.UserRoleResponse::role);
    }
  }

  @Nested
  @DisplayName("createUserRoles")
  class CreateUserRoles {

    @Test
    @DisplayName("should delegate to userRoleService and map responses")
    void shouldDelegateAndMapResponses() {
      // Given
      final var request = new CreateUserRolesRequest(USER_ID, List.of(Role.STUDENT, Role.TEACHER));
      final var role1 = buildUserRole();
      final var role2 = UserRoleJpa.builder().id("role-002").userId(USER_ID).role(Role.TEACHER).build();

      when(userRoleService.createUserRoles(USER_ID, List.of(Role.STUDENT, Role.TEACHER)))
          .thenReturn(List.of(role1, role2));

      // When
      final var result = wsOps.createUserRoles(request);

      // Then
      assertThat(result).hasSize(2);
    }
  }

  @Nested
  @DisplayName("getRolesByUserId")
  class GetRolesByUserId {

    @Test
    @DisplayName("should return list of role responses")
    void shouldReturnListOfRoleResponses() {
      // Given
      final var userRole = buildUserRole();
      when(userRoleService.getRoleByUserId(USER_ID)).thenReturn(List.of(userRole));

      // When
      final var result = wsOps.getRolesByUserId(USER_ID);

      // Then
      assertThat(result).hasSize(1);
      assertThat(result.get(0))
          .returns(ROLE_ID, edu.univalle.gadim.virtual_lab_platform.users.web.model.UserRoleResponse::id)
          .returns(Role.STUDENT, edu.univalle.gadim.virtual_lab_platform.users.web.model.UserRoleResponse::role);
    }
  }

  @Nested
  @DisplayName("updateUser")
  class UpdateUser {

    @Test
    @DisplayName("should delegate update and return updated response")
    void shouldDelegateUpdateAndReturnResponse() {
      // Given
      final var existing = buildUser();
      final var request = new UpdateUserRequest("Maria", null, null, null, null);
      final var updated = UserJpa.builder()
          .id(USER_ID)
          .name("Maria")
          .lastName("Martinez")
          .externalCode("EXT-001")
          .password("encoded")
          .status(UserStatus.ACTIVE)
          .createdDate(existing.createdDate())
          .build();

      when(userService.getUserById(USER_ID)).thenReturn(Optional.of(existing));
      when(userService.updateUser(anyString(), any())).thenReturn(updated);

      // When
      final var result = wsOps.updateUser(USER_ID, request);

      // Then
      assertThat(result)
          .returns(USER_ID, edu.univalle.gadim.virtual_lab_platform.users.web.model.UserResponse::id)
          .returns("Maria", edu.univalle.gadim.virtual_lab_platform.users.web.model.UserResponse::name);
      verify(userService).updateUser(anyString(), any());
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when user not found")
    void shouldThrowWhenUserNotFound() {
      // Given
      final var request = new UpdateUserRequest("Maria", null, null, null, null);
      when(userService.getUserById("nonexistent")).thenReturn(Optional.empty());

      // When / Then
      assertThatThrownBy(() -> wsOps.updateUser("nonexistent", request))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("nonexistent");
    }
  }

  @Nested
  @DisplayName("deleteUser")
  class DeleteUser {

    @Test
    @DisplayName("should delegate to userService")
    void shouldDelegateToUserService() {
      // When
      wsOps.deleteUser(USER_ID);

      // Then
      verify(userService).deleteUser(USER_ID);
    }
  }

  @Nested
  @DisplayName("deleteUserRole")
  class DeleteUserRole {

    @Test
    @DisplayName("should delegate to userRoleService")
    void shouldDelegateToUserRoleService() {
      // When
      wsOps.deleteUserRole(ROLE_ID);

      // Then
      verify(userRoleService).deleteUserRole(ROLE_ID);
    }
  }
}
