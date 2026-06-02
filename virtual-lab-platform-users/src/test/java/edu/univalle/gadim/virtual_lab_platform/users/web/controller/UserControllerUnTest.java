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

  private static final String USER_ID = "ana.martinez@correounivalle.edu.co";

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
      final var request =
          new CreateUserRequest(USER_ID, "Ana", "Martinez", null, "pass123", UserStatus.ACTIVE);
      final var response = buildUserResponse();
      when(usersWsOps.createUser(request)).thenReturn(response);

      final var result = controller.createUser(request);

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
      final var response = buildUserResponse();
      when(usersWsOps.getUserById(USER_ID)).thenReturn(response);

      final var result = controller.getUser(USER_ID);

      assertThat(result.getStatusCode().value()).isEqualTo(200);
      assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    @DisplayName("should return 404 when user not found")
    void shouldReturn404WhenNotFound() {
      when(usersWsOps.getUserById("nonexistent@correounivalle.edu.co"))
          .thenThrow(new IllegalArgumentException("not found"));

      final var result = controller.getUser("nonexistent@correounivalle.edu.co");

      assertThat(result.getStatusCode().value()).isEqualTo(404);
    }
  }

  @Nested
  @DisplayName("getAllUsers")
  class GetAllUsers {

    @Test
    @DisplayName("should return 200 with list of users")
    void shouldReturn200WithListOfUsers() {
      final var response = buildUserResponse();
      when(usersWsOps.getAllUsers()).thenReturn(List.of(response));

      final var result = controller.getAllUsers();

      assertThat(result.getStatusCode().value()).isEqualTo(200);
      assertThat(result.getBody()).hasSize(1);
    }
  }

  @Nested
  @DisplayName("updateUser")
  class UpdateUser {

    @Test
    @DisplayName("should return 200 with updated user")
    void shouldReturn200WithUpdatedUser() {
      final var request = new UpdateUserRequest("Maria", null, null, null, null);
      final var response = buildUserResponse();
      when(usersWsOps.updateUser(USER_ID, request)).thenReturn(response);

      final var result = controller.updateUser(USER_ID, request);

      assertThat(result.getStatusCode().value()).isEqualTo(200);
      assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    @DisplayName("should return 404 when user not found")
    void shouldReturn404WhenNotFound() {
      final var request = new UpdateUserRequest("Maria", null, null, null, null);
      when(usersWsOps.updateUser("nonexistent@correounivalle.edu.co", request))
          .thenThrow(new IllegalArgumentException("not found"));

      final var result = controller.updateUser("nonexistent@correounivalle.edu.co", request);

      assertThat(result.getStatusCode().value()).isEqualTo(404);
    }
  }

  @Nested
  @DisplayName("deleteUser")
  class DeleteUser {

    @Test
    @DisplayName("should return 204 on successful deletion")
    void shouldReturn204OnSuccess() {
      doNothing().when(usersWsOps).deleteUser(USER_ID);

      final var result = controller.deleteUser(USER_ID);

      assertThat(result.getStatusCode().value()).isEqualTo(204);
      verify(usersWsOps).deleteUser(USER_ID);
    }

    @Test
    @DisplayName("should return 404 when user not found")
    void shouldReturn404WhenNotFound() {
      doThrow(new IllegalArgumentException("not found")).when(usersWsOps).deleteUser("nonexistent@correounivalle.edu.co");

      final var result = controller.deleteUser("nonexistent@correounivalle.edu.co");

      assertThat(result.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    @DisplayName("should return 409 when user is not INACTIVE")
    void shouldReturn409WhenNotInactive() {
      doThrow(new IllegalStateException("not inactive")).when(usersWsOps).deleteUser(USER_ID);

      final var result = controller.deleteUser(USER_ID);

      assertThat(result.getStatusCode().value()).isEqualTo(409);
    }
  }
}