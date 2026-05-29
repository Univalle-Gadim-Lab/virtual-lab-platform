package edu.univalle.gadim.virtual_lab_platform.users.web.controller;

import edu.univalle.gadim.virtual_lab_platform.users.web.model.CreateUserRequest;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.UpdateUserRequest;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.UserResponse;
import edu.univalle.gadim.virtual_lab_platform.users.web.ops.UsersWsOps;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for user management operations.
 *
 * <p>This controller provides endpoints for creating, retrieving, and listing users.
 * All operations are delegated to {@link UsersWsOps}, keeping this class as a thin
 * HTTP adapter that handles request routing and response status mapping.
 *
 * <h2>Endpoints</h2>
 * <ul>
 *   <li>{@code POST /api/users} — create a new user</li>
 *   <li>{@code GET /api/users/{id}} — retrieve a user by ID</li>
 *   <li>{@code GET /api/users} — list all users</li>
 *   <li>{@code GET /api/users/by-username} — retrieve a user by username</li>
 * </ul>
 *
 * @see UsersWsOps
 */
@RestController
@RequestMapping("/api/users")
@ParametersAreNonnullByDefault
public class UserController {

    private final UsersWsOps usersWsOps;

    /**
     * Constructs a new {@code UserController} with the required operations dependency.
     *
     * @param usersWsOps the web service operations interface for user management
     */
    public UserController(UsersWsOps usersWsOps) {
        this.usersWsOps = usersWsOps;
    }

    /**
     * Creates a new user.
     *
     * @param request the create user request containing user details
     * @return a {@code 200 OK} response with the created user data
     */
    @PostMapping
    @Nonnull
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(usersWsOps.createUser(request));
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the user ID extracted from the path
     * @return a {@code 200 OK} response with the user data, or {@code 404 Not Found}
     *     if no user exists with the given ID
     */
    @GetMapping("/{id}")
    @Nonnull
    public ResponseEntity<UserResponse> getUser(@PathVariable @Nonnull String id) {
        try {
            return ResponseEntity.ok(usersWsOps.getUserById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves all users from the system.
     *
     * @return a {@code 200 OK} response with the list of all user responses
     */
    @GetMapping
    @Nonnull
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(usersWsOps.getAllUsers());
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username query parameter to search for
     * @return a {@code 200 OK} response with the user data, or {@code 404 Not Found}
     *     if no user exists with the given username
     */
    @GetMapping("/by-username")
    @Nonnull
    public ResponseEntity<UserResponse> getUserByUsername(@RequestParam String username) {
        try {
            return ResponseEntity.ok(usersWsOps.getUserByUsername(username));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Updates an existing user's mutable fields.
     *
     * @param id the user ID extracted from the path
     * @param request the update user request containing fields to update
     * @return a {@code 200 OK} response with the updated user data, or {@code 404 Not Found}
     *     if no user exists with the given ID
     */
    @PutMapping("/{id}")
    @Nonnull
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable @Nonnull String id, @RequestBody UpdateUserRequest request) {
        try {
            return ResponseEntity.ok(usersWsOps.updateUser(id, request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Marks a user as deleted by transitioning their status to {@code DELETED}.
     *
     * <p>The user must currently be in {@code INACTIVE} status. Returns
     * {@code 409 Conflict} if the precondition is not met.
     *
     * @param id the user ID extracted from the path
     * @return a {@code 204 No Content} response on success, {@code 404 Not Found}
     *     if no user exists, or {@code 409 Conflict} if the user is not INACTIVE
     */
    @DeleteMapping("/{id}")
    @Nonnull
    public ResponseEntity<Void> deleteUser(@PathVariable @Nonnull String id) {
        try {
            usersWsOps.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).build();
        }
    }
}
