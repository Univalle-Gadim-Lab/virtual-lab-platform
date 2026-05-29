package edu.univalle.gadim.virtual_lab_platform.users.web.controller;

import edu.univalle.gadim.virtual_lab_platform.users.web.model.CreateUserRolesRequest;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.CreateUserRoleRequest;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.UserRoleResponse;
import edu.univalle.gadim.virtual_lab_platform.users.web.ops.UsersWsOps;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for user role management operations.
 *
 * <p>This controller provides endpoints for creating and retrieving user roles.
 * All operations are delegated to {@link UsersWsOps}, keeping this class as a thin
 * HTTP adapter that handles request routing and response status mapping.
 *
 * <h2>Endpoints</h2>
 * <ul>
 *   <li>{@code POST /api/user-roles} — assign a single role to a user</li>
 *   <li>{@code POST /api/user-roles/batch} — assign multiple roles to a user</li>
 *   <li>{@code GET /api/user-roles} — list all roles for a user</li>
 * </ul>
 *
 * @see UsersWsOps
 */
@RestController
@RequestMapping("/api/user-roles")
@ParametersAreNonnullByDefault
public class UserRoleController {

    private final UsersWsOps usersWsOps;

    /**
     * Constructs a new {@code UserRoleController} with the required operations dependency.
     *
     * @param usersWsOps the web service operations interface for role management
     */
    public UserRoleController(UsersWsOps usersWsOps) {
        this.usersWsOps = usersWsOps;
    }

    /**
     * Assigns a single role to the specified user.
     *
     * @param request the create user role request containing user ID and role to assign
     * @return a {@code 200 OK} response with the created user role data
     */
    @PostMapping
    @Nonnull
    public ResponseEntity<UserRoleResponse> createUserRole(
            @RequestBody @Nonnull CreateUserRoleRequest request) {
        return ResponseEntity.ok(usersWsOps.createUserRole(request));
    }

    /**
     * Assigns multiple roles to the specified user in a single operation.
     *
     * @param request the create user roles request containing user ID and list of roles
     * @return a {@code 200 OK} response with the list of created user role data
     */
    @PostMapping("/batch")
    @Nonnull
    public ResponseEntity<List<UserRoleResponse>> createUserRoles(
            @RequestBody @Nonnull CreateUserRolesRequest request) {
        return ResponseEntity.ok(usersWsOps.createUserRoles(request));
    }

    /**
     * Retrieves all roles assigned to the specified user.
     *
     * @param userId the user ID query parameter to retrieve roles for
     * @return a {@code 200 OK} response with the list of user role responses for the user
     */
    @GetMapping
    @Nonnull
    public ResponseEntity<List<UserRoleResponse>> getRolesByUserId(
            @RequestParam @Nonnull String userId) {
        return ResponseEntity.ok(usersWsOps.getRolesByUserId(userId));
    }

    /**
     * Removes a role assignment by its unique identifier.
     *
     * @param id the role assignment ID extracted from the path
     * @return a {@code 204 No Content} response on success, or {@code 404 Not Found}
     *     if no role assignment exists with the given ID
     */
    @DeleteMapping("/{id}")
    @Nonnull
    public ResponseEntity<Void> deleteUserRole(@PathVariable @Nonnull String id) {
        try {
            usersWsOps.deleteUserRole(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
