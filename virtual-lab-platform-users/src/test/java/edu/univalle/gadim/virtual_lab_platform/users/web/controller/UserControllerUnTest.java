package edu.univalle.gadim.virtual_lab_platform.users.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.univalle.gadim.virtual_lab_platform.users.api.type.UserStatus;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.CreateUserRequest;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.UpdateUserRequest;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.UserResponse;
import edu.univalle.gadim.virtual_lab_platform.users.web.ops.UsersWsOps;
import java.time.LocalDateTime;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@NullMarked
@DisplayName("UserController")
class UserControllerUnTest {

  private static final String USER_ID = "user-001";

  private UsersWsOps usersWsOps;
  private UserController controller;

  @BeforeEach
  void setUp() {
    usersWsOps = mock(UsersWsOps.class);
    controller = new UserController(usersWsOps);
  }

  private UserResponse buildUserResponse() {
    return new UserResponse(USER_ID, "Ana", "Martinez", "EXT-001", UserStatus.ACTIVE,
        LocalDateTime.of(2025, 1, 15, 10, 30, 0));
  }

  @Nested
  @DisplayName("createUser")
  class CreateUser {

    @Test
    @DisplayName("should return 200 with created user")
    void shouldReturn200WithCreatedUser() {
      // Given
      final var request = new CreateUserRequest("Ana", "Martinez", null, "pass123", UserStatus.ACTIVE);
      final var response = buildUserResponse();
      when(usersWsOps.createUser(request)).thenReturn(response);

      // When
      final var result = controller.createUser(request);

      // Then
      assertThat(result.getStatusCode().value()).isEqualTo(200);
      assertThat(result.getBody()).isEqualTo(response);
    }
  }

  @Nested
  @DisplayName("getUser")
  class GetUser {

    @Test
    @DisplayName("should return 200 when user found")
    void shouldReturn200WhenFound() {
      // Given
      final var response = buildUserResponse();
      when(usersWsOps.getUserById(USER_ID)).thenReturn(response);

      // When
      final var result = controller.getUser(USER_ID);

      // Then
      assertThat(result.getStatusCode().value()).isEqualTo(200);
      assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    @DisplayName("should return 404 when user not found")
    void shouldReturn404WhenNotFound() {
      // Given
      when(usersWsOps.getUserById("nonexistent")).thenThrow(new IllegalArgumentException("not found"));

      // When
      final var result = controller.getUser("nonexistent");

      // Then
      assertThat(result.getStatusCode().value()).isEqualTo(404);
    }
  }

  @Nested
  @DisplayName("getAllUsers")
  class GetAllUsers {

    @Test
    @DisplayName("should return 200 with list of users")
    void shouldReturn200WithListOfUsers() {
      // Given
      final var response = buildUserResponse();
      when(usersWsOps.getAllUsers()).thenReturn(List.of(response));

      // When
      final var result = controller.getAllUsers();

      // Then
      assertThat(result.getStatusCode().value()).isEqualTo(200);
      assertThat(result.getBody()).hasSize(1);
    }
  }

  @Nested
  @DisplayName("getUserByUsername")
  class GetUserByUsername {

    @Test
    @DisplayName("should return 200 when user found")
    void shouldReturn200WhenFound() {
      // Given
      final var response = buildUserResponse();
      when(usersWsOps.getUserByUsername("Ana")).thenReturn(response);

      // When
      final var result = controller.getUserByUsername("Ana");

      // Then
      assertThat(result.getStatusCode().value()).isEqualTo(200);
      assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    @DisplayName("should return 404 when user not found")
    void shouldReturn404WhenNotFound() {
      // Given
      when(usersWsOps.getUserByUsername("nonexistent")).thenThrow(new IllegalArgumentException("not found"));

      // When
      final var result = controller.getUserByUsername("nonexistent");

      // Then
      assertThat(result.getStatusCode().value()).isEqualTo(404);
    }
  }

  @Nested
  @DisplayName("updateUser")
  class UpdateUser {

    @Test
    @DisplayName("should return 200 with updated user")
    void shouldReturn200WithUpdatedUser() {
      // Given
      final var request = new UpdateUserRequest("Maria", null, null, null, null);
      final var response = buildUserResponse();
      when(usersWsOps.updateUser(USER_ID, request)).thenReturn(response);

      // When
      final var result = controller.updateUser(USER_ID, request);

      // Then
      assertThat(result.getStatusCode().value()).isEqualTo(200);
      assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    @DisplayName("should return 404 when user not found")
    void shouldReturn404WhenNotFound() {
      // Given
      final var request = new UpdateUserRequest("Maria", null, null, null, null);
      when(usersWsOps.updateUser("nonexistent", request)).thenThrow(new IllegalArgumentException("not found"));

      // When
      final var result = controller.updateUser("nonexistent", request);

      // Then
      assertThat(result.getStatusCode().value()).isEqualTo(404);
    }
  }

  @Nested
  @DisplayName("deleteUser")
  class DeleteUser {

    @Test
    @DisplayName("should return 204 on successful deletion")
    void shouldReturn204OnSuccess() {
      // Given
      doNothing().when(usersWsOps).deleteUser(USER_ID);

      // When
      final var result = controller.deleteUser(USER_ID);

      // Then
      assertThat(result.getStatusCode().value()).isEqualTo(204);
      verify(usersWsOps).deleteUser(USER_ID);
    }

    @Test
    @DisplayName("should return 404 when user not found")
    void shouldReturn404WhenNotFound() {
      // Given
      doThrow(new IllegalArgumentException("not found")).when(usersWsOps).deleteUser("nonexistent");

      // When
      final var result = controller.deleteUser("nonexistent");

      // Then
      assertThat(result.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    @DisplayName("should return 409 when user is not INACTIVE")
    void shouldReturn409WhenNotInactive() {
      // Given
      doThrow(new IllegalStateException("not inactive")).when(usersWsOps).deleteUser(USER_ID);

      // When
      final var result = controller.deleteUser(USER_ID);

      // Then
      assertThat(result.getStatusCode().value()).isEqualTo(409);
    }
  }
}
