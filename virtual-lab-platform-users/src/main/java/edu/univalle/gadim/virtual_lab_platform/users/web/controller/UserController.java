package edu.univalle.gadim.virtual_lab_platform.users.web.controller;

import edu.univalle.gadim.virtual_lab_platform.users.api.service.UserService;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.User;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.UserStatus;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.CreateUserRequest;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.UserResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for user management operations.
 *
 * <p>This controller provides endpoints for creating, retrieving, and listing users.
 */
@RestController
@RequestMapping("/api/users")
@ParametersAreNonnullByDefault
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  /**
   * Creates a new user.
   *
   * @param request the create user request containing user details
   * @return the created user response
   */
  @PostMapping
  @Nonnull
  public ResponseEntity<UserResponse> createUser(@RequestBody @Nonnull CreateUserRequest request) {
    User user =
        new UserCreateRecord(
            "temp",
            request.name(),
            request.lastName(),
            Optional.ofNullable(request.externalCode()),
            request.password(),
            request.status(),
            LocalDateTime.now());

    User created = userService.createUser(user);
    UserResponse response =
        new UserResponse(
            created.id(),
            created.name(),
            created.lastName(),
            created.externalCode().orElse(null),
            created.status(),
            created.createdDate());

    return ResponseEntity.ok(response);
  }

  /**
   * Retrieves a user by ID.
   *
   * @param id the user ID
   * @return the user response or 404 if not found
   */
  @GetMapping("/{id}")
  @Nonnull
  public ResponseEntity<UserResponse> getUser(@PathVariable @Nonnull String id) {
    return userService
        .getUserById(id)
        .map(
            user ->
                ResponseEntity.ok(
                    new UserResponse(
                        user.id(),
                        user.name(),
                        user.lastName(),
                        user.externalCode().orElse(null),
                        user.status(),
                        user.createdDate())))
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * Retrieves all users.
   *
   * @return the list of user responses
   */
  @GetMapping
  @Nonnull
  public ResponseEntity<List<UserResponse>> getAllUsers() {
    List<UserResponse> responses =
        userService.getAllUsers().stream()
            .map(
                user ->
                    new UserResponse(
                        user.id(),
                        user.name(),
                        user.lastName(),
                        user.externalCode().orElse(null),
                        user.status(),
                        user.createdDate()))
            .toList();

    return ResponseEntity.ok(responses);
  }

  /**
   * Retrieves a user by username.
   *
   * @param username the username to search for
   * @return the user response or 404 if not found
   */
  @GetMapping("/by-username")
  @Nonnull
  public ResponseEntity<UserResponse> getUserByUsername(@RequestParam String username) {
    return userService
        .getUserByUsername(username)
        .map(
            user ->
                ResponseEntity.ok(
                    new UserResponse(
                        user.id(),
                        user.name(),
                        user.lastName(),
                        user.externalCode().orElse(null),
                        user.status(),
                        user.createdDate())))
        .orElse(ResponseEntity.notFound().build());
  }

  /** Private record implementing User interface for create operations. */
  private record UserCreateRecord(
      String id,
      String name,
      String lastName,
      Optional<String> externalCode,
      String password,
      UserStatus status,
      LocalDateTime createdDate)
      implements User {}
}
