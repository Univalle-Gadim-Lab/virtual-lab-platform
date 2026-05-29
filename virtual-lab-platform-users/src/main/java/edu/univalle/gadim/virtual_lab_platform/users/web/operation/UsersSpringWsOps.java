package edu.univalle.gadim.virtual_lab_platform.users.web.operation;

import edu.univalle.gadim.virtual_lab_platform.users.api.service.UserRoleService;
import edu.univalle.gadim.virtual_lab_platform.users.api.service.UserService;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.User;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.UserStatus;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.CreateUserRequest;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.CreateUserRolesRequest;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.CreateUserRoleRequest;
import edu.univalle.gadim.virtual_lab_platform.users.web.model.UpdateUserRequest;
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
public class UsersSpringWsOps implements UsersWsOps {

    private final UserService userService;
    private final UserRoleService userRoleService;

    /**
     * Constructs a new {@code UsersSpringWsOps} with the required service dependencies.
     *
     * @param userService the user domain service for user lifecycle operations
     * @param userRoleService the user role domain service for role assignment operations
     */
    public UsersSpringWsOps(UserService userService, UserRoleService userRoleService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

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

    @Override
    @Nonnull
    public UserResponse getUserById(@Nonnull String id) {
        return userService.getUserById(id)
                .map(this::toUserResponse)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }

    @Override
    @Nonnull
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(this::toUserResponse)
                .toList();
    }

    @Override
    @Nonnull
    public UserResponse getUserByUsername(@Nonnull String username) {
        return userService.getUserByUsername(username)
                .map(this::toUserResponse)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }

    @Override
    @Nonnull
    public UserRoleResponse createUserRole(@Nonnull CreateUserRoleRequest request) {
        final var userRole = userRoleService.createUserRole(request.userId(), request.role());
        return new UserRoleResponse(userRole.id(), userRole.userId(), userRole.role());
    }

    @Override
    @Nonnull
    public List<UserRoleResponse> createUserRoles(@Nonnull CreateUserRolesRequest request) {
        return userRoleService.createUserRoles(request.userId(), request.roles()).stream()
                .map(ur -> new UserRoleResponse(ur.id(), ur.userId(), ur.role()))
                .toList();
    }

    @Override
    @Nonnull
    public List<UserRoleResponse> getRolesByUserId(@Nonnull String userId) {
        return userRoleService.getRoleByUserId(userId).stream()
                .map(ur -> new UserRoleResponse(ur.id(), ur.userId(), ur.role()))
                .toList();
    }

    @Override
    @Nonnull
    public UserResponse updateUser(@Nonnull String id, @Nonnull UpdateUserRequest request) {
        final var existing = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        final var updated = new UserUpdateRecord(
                existing.id(),
                request.name() != null ? request.name() : existing.name(),
                request.lastName() != null ? request.lastName() : existing.lastName(),
                request.externalCode() != null
                        ? Optional.of(request.externalCode())
                        : existing.externalCode(),
                request.password() != null ? request.password() : "",
                request.status() != null ? request.status() : existing.status(),
                existing.createdDate());

        final var result = userService.updateUser(id, updated);
        return toUserResponse(result);
    }

    @Override
    public void deleteUser(@Nonnull String id) {
        userService.deleteUser(id);
    }

    @Override
    public void deleteUserRole(@Nonnull String id) {
        userRoleService.deleteUserRole(id);
    }

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

    private record UserUpdateRecord(
            String id,
            String name,
            String lastName,
            Optional<String> externalCode,
            String password,
            UserStatus status,
            LocalDateTime createdDate)
            implements User {}
}
