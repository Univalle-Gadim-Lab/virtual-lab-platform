package edu.univalle.gadim.virtual_lab_platform.instances.web.operation;

import edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceUserService;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.CreateInstanceUserRequest;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.InstanceUserResponse;
import edu.univalle.gadim.virtual_lab_platform.instances.web.ops.InstanceUsersWsOps;
import java.util.List;
import javax.annotation.Nonnull;
import org.springframework.stereotype.Component;

/**
 * Concrete implementation of {@link InstanceUsersWsOps} that delegates to the
 * {@link InstanceUserService} domain service.
 *
 * <p>This class acts as the bridge between the HTTP contract layer and the
 * business logic layer. It translates incoming request DTOs into domain parameters,
 * invokes the appropriate service methods, and maps the resulting domain objects
 * back into response DTOs suitable for HTTP serialization.
 *
 * @see InstanceUsersWsOps
 * @see InstanceUserService
 */
@Component
public class InstanceUsersSpringWsOps implements InstanceUsersWsOps {

  private final InstanceUserService instanceUserService;

  public InstanceUsersSpringWsOps(InstanceUserService instanceUserService) {
    this.instanceUserService = instanceUserService;
  }

  @Override
  @Nonnull
  public InstanceUserResponse assignUserToInstance(@Nonnull CreateInstanceUserRequest request) {
    final var instanceUser =
        instanceUserService.assignUserToInstance(request.userId(), request.instanceId());
    return InstanceUserResponse.from(instanceUser);
  }

  @Override
  @Nonnull
  public List<InstanceUserResponse> getUsersByInstanceId(@Nonnull String instanceId) {
    return instanceUserService.getUsersByInstanceId(instanceId).stream()
        .map(InstanceUserResponse::from)
        .toList();
  }

  @Override
  @Nonnull
  public List<InstanceUserResponse> getInstancesByUserId(@Nonnull String userId) {
    return instanceUserService.getInstancesByUserId(userId).stream()
        .map(InstanceUserResponse::from)
        .toList();
  }

  @Override
  public void removeUserFromInstance(@Nonnull String userId, @Nonnull String instanceId) {
    instanceUserService.removeUserFromInstance(userId, instanceId);
  }
}
