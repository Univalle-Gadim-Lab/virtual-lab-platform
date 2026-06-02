package edu.univalle.gadim.virtual_lab_platform.users.web.ops;

import edu.univalle.gadim.virtual_lab_platform.users.web.model.CreateUserRequest;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.CreateUserRolesRequest;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.CreateUserRoleRequest;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.UpdateUserRequest;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.UserResponse;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.UserRoleResponse;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * Web service operations contract for user and role management.
 *
 * <p>Defines one method per web endpoint exposed by the users module REST API.
 * Implementations bridge the HTTP layer to the underlying {@link edu.univalle.gadim.virtual_lab_platform.users.api.service.UserService}
 * and {@link edu.univalle.gadim.virtual_lab_platform.users.api.service.UserRoleService} domain services,
 * performing request-to-domain translation and domain-to-response mapping.
 *
 * <h2>Endpoints</h2>
 * <ul>
 *   <li>{@code POST /api/users} — create a new user</li>
 *   <li>{@code GET /api/users/{id}} — retrieve a user by ID (institutional email)</li>
 *   <li>{@code GET /api/users} — list all users</li>
 *   <li>{@code POST /api/user-roles} — assign a single role to a user</li>
 *   <li>{@code POST /api/user-roles/batch} — assign multiple roles to a user</li>
 *   <li>{@code GET /api/user-roles} — list all roles for a user</li>
 * </ul>
 */
public interface UsersWsOps {

    /**
     * Creates a new user with the provided information.
     *
     * @param request the create user request containing institutional email (id),
     *     name, last name, optional external code, password, and status
     * @return the created user response with creation date
     */
    @Nonnull
    UserResponse createUser(@Nonnull CreateUserRequest request);

    /**
     * Retrieves a user by their unique identifier (institutional email).
     *
     * @param id the unique user identifier (institutional email)
     * @return the user response matching the given ID
     * @throws IllegalArgumentException if no user is found with the given ID
     */
    @Nonnull
    UserResponse getUserById(@Nonnull String id);

    /**
     * Retrieves all users from the system.
     *
     * @return the list of all user responses, never null but may be empty
     */
    @Nonnull
    List<UserResponse> getAllUsers();

    /**
     * Assigns a single role to the specified user.
     *
     * @param request the create user role request containing user ID and role to assign
     * @return the created user role response with generated ID
     */
    @Nonnull
    UserRoleResponse createUserRole(@Nonnull CreateUserRoleRequest request);

    /**
     * Assigns multiple roles to the specified user in a single operation.
     *
     * @param request the create user roles request containing user ID and list of roles to assign
     * @return the list of created user role responses with generated IDs
     */
    @Nonnull
    List<UserRoleResponse> createUserRoles(@Nonnull CreateUserRolesRequest request);

    /**
     * Retrieves all roles assigned to the specified user.
     *
     * @param userId the ID of the user to retrieve roles for
     * @return the list of user role responses for the user, never null but may be empty
     */
    @Nonnull
    List<UserRoleResponse> getRolesByUserId(@Nonnull String userId);

    /**
     * Updates an existing user's mutable fields.
     *
     * @param id the unique identifier of the user to update
     * @param request the update user request containing fields to update
     * @return the updated user response
     * @throws IllegalArgumentException if no user is found with the given ID
     */
    @Nonnull
    UserResponse updateUser(@Nonnull String id, @Nonnull UpdateUserRequest request);

    /**
     * Marks a user as deleted by transitioning their status to {@code DELETED}.
     *
     * <p>The user must currently be in {@code INACTIVE} status.
     *
     * @param id the unique identifier of the user to delete
     * @throws IllegalArgumentException if no user is found with the given ID
     * @throws IllegalStateException if the user is not currently {@code INACTIVE}
     */
    void deleteUser(@Nonnull String id);

    /**
     * Removes a role assignment by its unique identifier.
     *
     * @param id the unique identifier of the role assignment to remove
     * @throws IllegalArgumentException if no role assignment exists with the given ID
     */
    void deleteUserRole(@Nonnull String id);
}
