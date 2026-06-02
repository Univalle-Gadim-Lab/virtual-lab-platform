package edu.univalle.gadim.virtual_lab_platform.authentication.web.operation;

import edu.univalle.gadim.virtual_lab_platform.authentication.api.service.AuthenticationService;
import edu.univalle.gadim.virtual_lab_platform.authentication.api.service.TokenService;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.AuthenticatedUserResponse;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.LoginRequest;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.LoginResponse;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.LogoutRequest;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.model.RefreshTokenRequest;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.ops.AuthWsOps;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.Role;
import edu.univalle.gadim.virtual_lab_platform.users.data.repository.UserRepository;
import edu.univalle.gadim.virtual_lab_platform.users.data.repository.UserRoleRepository;
import java.util.EnumSet;
import java.util.Set;
import javax.annotation.Nonnull;
import org.springframework.stereotype.Service;

/**
 * Concrete implementation of {@link AuthWsOps} that delegates to the
 * {@link AuthenticationService} and {@link TokenService} domain services.
 *
 * <p>This class acts as the bridge between the HTTP contract layer and the
 * business logic layer. It translates incoming request DTOs into domain parameters,
 * invokes the appropriate service methods, and maps the resulting domain objects
 * back into response DTOs suitable for HTTP serialization.
 *
 * @see AuthWsOps
 * @see AuthenticationService
 * @see TokenService
 */
@Service
public class AuthSpringWsOps implements AuthWsOps {

  private final AuthenticationService authenticationService;
  private final UserRepository userRepository;
  private final UserRoleRepository userRoleRepository;

  /**
   * Constructs a new {@code AuthSpringWsOps} with the required dependencies.
   *
   * @param authenticationService the authentication domain service
   * @param userRepository        the user repository for current user lookup
   * @param userRoleRepository    the user role repository for role resolution
   */
  public AuthSpringWsOps(
      AuthenticationService authenticationService,
      UserRepository userRepository,
      UserRoleRepository userRoleRepository) {
    this.authenticationService = authenticationService;
    this.userRepository = userRepository;
    this.userRoleRepository = userRoleRepository;
  }

  @Override
  @Nonnull
  public LoginResponse login(@Nonnull LoginRequest request) {
    final var result =
        authenticationService.login(request.email(), request.password());
    return new LoginResponse(
        result.accessToken(),
        result.refreshToken(),
        result.tokenType(),
        result.expiresIn());
  }

  @Override
  @Nonnull
  public LoginResponse refresh(@Nonnull RefreshTokenRequest request) {
    final var result = authenticationService.refresh(request.refreshToken());
    return new LoginResponse(
        result.accessToken(),
        result.refreshToken(),
        result.tokenType(),
        result.expiresIn());
  }

  @Override
  public void logout(@Nonnull LogoutRequest request) {
    authenticationService.logout(request.refreshToken());
  }

  @Override
  @Nonnull
  public AuthenticatedUserResponse me(@Nonnull String userId) {
    final var user =
        userRepository
            .findById(userId)
            .orElseThrow(
                () -> new IllegalArgumentException("User not found: " + userId));

    final var roles =
        userRoleRepository.findByUserId(userId).stream()
            .map(ur -> ur.role())
            .collect(() -> EnumSet.noneOf(Role.class), Set::add, Set::addAll);

    return new AuthenticatedUserResponse(
        user.id(), user.name(), user.lastName(), roles);
  }
}
