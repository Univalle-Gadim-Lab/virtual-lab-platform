package edu.univalle.gadim.virtual_lab_platform.users.web.operation;

import edu.univalle.gadim.virtual_lab_platform.users.api.service.UserRoleService;
import edu.univalle.gadim.virtual_lab_platform.users.api.service.UserService;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.User;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.UserStatus;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.CreateUserRequest;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.CreateUserRolesRequest;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.CreateUserRoleRequest;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.UserResponse;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.UserRoleResponse;
import edu.univalle.gadim.virtual_lab_platform.users.web.ops.UsersWsOps;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.springframework.stereotype.Service;

/**
 * Concrete implementation of {@link UsersWsOps} that delegates to the
 * {@link UserService} and {@link UserRoleService} domain services.
 *
 * <p>This class acts as the bridge between the HTTP contract layer and the
 * business logic layer. It translates incoming request DTOs into domain objects,
 * invokes the appropriate service methods, and maps the resulting domain objects
 * back into response DTOs suitable for HTTP serialization.
 *
 * @see UsersWsOps
 * @see UserService
 * @see UserRoleService
 */
@Service
public class UsersWsOpsImpl implements UsersWsOps {

    private final UserService userService;
    private final UserRoleService userRoleService;

    /**
     * Constructs a new {@code UsersWsOpsImpl} with the required service dependencies.
     *
     * @param userService the user domain service for user lifecycle operations
     * @param userRoleService the user role domain service for role assignment operations
     */
    public UsersWsOpsImpl(UserService userService, UserRoleService userRoleService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    /**
     * Creates a new user by translating the request DTO into a domain object
     * and delegating to {@link UserService#createUser(User)}.
     *
     * @param request the create user request containing user details
     * @return the created user response with generated ID and creation date
     */
    @Override
    @Nonnull
    public UserResponse createUser(@Nonnull CreateUserRequest request) {
        final var user = new UserCreateRecord(
                "temp",
                request.name(),
                request.lastName(),
                Optional.ofNullable(request.externalCode()),
                request.password(),
                request.status(),
                LocalDateTime.now());

        final var created = userService.createUser(user);
        return toUserResponse(created);
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique user identifier
     * @return the user response matching the given ID
     * @throws IllegalArgumentException if no user is found with the given ID
     */
    @Override
    @Nonnull
    public UserResponse getUserById(@Nonnull String id) {
        return userService.getUserById(id)
                .map(this::toUserResponse)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }

    /**
     * Retrieves all users from the system by delegating to
     * {@link UserService#getAllUsers()}.
     *
     * @return the list of all user responses, never null but may be empty
     */
    @Override
    @Nonnull
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(this::toUserResponse)
                .toList();
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username to search for
     * @return the user response matching the given username
     * @throws IllegalArgumentException if no user is found with the given username
     */
    @Override
    @Nonnull
    public UserResponse getUserByUsername(@Nonnull String username) {
        return userService.getUserByUsername(username)
                .map(this::toUserResponse)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }

    /**
     * Assigns a single role to the specified user by translating the request
     * and delegating to {@link UserRoleService#createUserRole(String, edu.univalle.gadim.virtual_lab_platform.users.api.type.Role)}.
     *
     * @param request the create user role request containing user ID and role
     * @return the created user role response with generated ID
     */
    @Override
    @Nonnull
    public UserRoleResponse createUserRole(@Nonnull CreateUserRoleRequest request) {
        final var userRole = userRoleService.createUserRole(request.userId(), request.role());
        return new UserRoleResponse(userRole.id(), userRole.userId(), userRole.role());
    }

    /**
     * Assigns multiple roles to the specified user by translating the request
     * and delegating to {@link UserRoleService#createUserRoles(String, List)}.
     *
     * @param request the create user roles request containing user ID and list of roles
     * @return the list of created user role responses with generated IDs
     */
    @Override
    @Nonnull
    public List<UserRoleResponse> createUserRoles(@Nonnull CreateUserRolesRequest request) {
        return userRoleService.createUserRoles(request.userId(), request.roles()).stream()
                .map(ur -> new UserRoleResponse(ur.id(), ur.userId(), ur.role()))
                .toList();
    }

    /**
     * Retrieves all roles assigned to the specified user by delegating to
     * {@link UserRoleService#getRoleByUserId(String)}.
     *
     * @param userId the ID of the user to retrieve roles for
     * @return the list of user role responses for the user, never null but may be empty
     */
    @Override
    @Nonnull
    public List<UserRoleResponse> getRolesByUserId(@Nonnull String userId) {
        return userRoleService.getRoleByUserId(userId).stream()
                .map(ur -> new UserRoleResponse(ur.id(), ur.userId(), ur.role()))
                .toList();
    }

    /**
     * Converts a {@link User} domain object into a {@link UserResponse} DTO
     * suitable for HTTP serialization.
     *
     * @param user the domain user object to convert
     * @return the corresponding response DTO
     */
    private UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.id(),
                user.name(),
                user.lastName(),
                user.externalCode().orElse(null),
                user.status(),
                user.createdDate());
    }

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
