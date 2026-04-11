package edu.univalle.gadim.virtual_lab_platform.users.web.controller;

import edu.univalle.gadim.virtual_lab_platform.users.api.service.UserRoleService;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.UserRole;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.CreateUserRoleRequest;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.CreateUserRolesRequest;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.UserRoleResponse;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for user role management operations.
 *
 * <p>This controller provides endpoints for creating and retrieving user roles.
 */
@RestController
@RequestMapping("/api/user-roles")
@ParametersAreNonnullByDefault
public class UserRoleController {

  private final UserRoleService userRoleService;

  public UserRoleController(UserRoleService userRoleService) {
    this.userRoleService = userRoleService;
  }

  /**
   * Creates a new user role.
   *
   * @param request the create user role request containing user ID and role
   * @return the created user role response
   */
  @PostMapping
  @Nonnull
  public ResponseEntity<UserRoleResponse> createUserRole(
      @RequestBody @Nonnull CreateUserRoleRequest request) {
    UserRole userRole = userRoleService.createUserRole(request.userId(), request.role());
    UserRoleResponse response =
        new UserRoleResponse(userRole.id(), userRole.userId(), userRole.role());
    return ResponseEntity.ok(response);
  }

  /**
   * Creates multiple user roles for a user.
   *
   * @param request the create user roles request containing user ID and list of roles
   * @return the list of created user role responses
   */
  @PostMapping("/batch")
  @Nonnull
  public ResponseEntity<List<UserRoleResponse>> createUserRoles(
      @RequestBody @Nonnull CreateUserRolesRequest request) {
    List<UserRole> userRoles =
        userRoleService.createUserRoles(request.userId(), request.roles());
    List<UserRoleResponse> responses =
        userRoles.stream()
            .map(userRole -> new UserRoleResponse(userRole.id(), userRole.userId(), userRole.role()))
            .toList();
    return ResponseEntity.ok(responses);
  }

  /**
   * Retrieves all roles for a specific user.
   *
   * @param userId the user ID to retrieve roles for
   * @return the list of user role responses for the user
   */
  @GetMapping
  @Nonnull
  public ResponseEntity<List<UserRoleResponse>> getRolesByUserId(
      @RequestParam @Nonnull String userId) {
    List<UserRole> userRoles = userRoleService.getRoleByUserId(userId);
    List<UserRoleResponse> responses =
        userRoles.stream()
            .map(userRole -> new UserRoleResponse(userRole.id(), userRole.userId(), userRole.role()))
            .toList();
    return ResponseEntity.ok(responses);
  }
}