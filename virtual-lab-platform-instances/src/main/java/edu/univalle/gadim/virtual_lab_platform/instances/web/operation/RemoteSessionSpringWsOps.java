package edu.univalle.gadim.virtual_lab_platform.instances.web.operation;

import edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceService;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.RemoteSessionResponse;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.RemoteSessionStatusResponse;
import edu.univalle.gadim.virtual_lab_platform.instances.web.ops.RemoteSessionWsOps;
import javax.annotation.Nonnull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Concrete implementation of {@link RemoteSessionWsOps} that delegates to
 * {@link InstanceService} for remote session operations.
 *
 * <p>Bridges the HTTP contract layer to the business logic layer, translating
 * session requests into service calls and mapping results to response DTOs.
 *
 * @see RemoteSessionWsOps
 * @see InstanceService
 */
@Component
public class RemoteSessionSpringWsOps implements RemoteSessionWsOps {

  private final InstanceService instanceService;

  public RemoteSessionSpringWsOps(InstanceService instanceService) {
    this.instanceService = instanceService;
  }

  @Override
  @Nonnull
  public RemoteSessionResponse getRemoteSession(@Nonnull String instanceId) {
    final var userId = currentUserId();
    final var instance = instanceService.getRemoteSessionInfo(instanceId, userId);
    return RemoteSessionResponse.from(instance);
  }

  @Override
  public void terminateRemoteSession(@Nonnull String instanceId) {
    instanceService.stopInstance(instanceId);
  }

  @Override
  @Nonnull
  public RemoteSessionStatusResponse getRemoteSessionStatus(@Nonnull String instanceId) {
    final var userId = currentUserId();
    final var vncReachable = instanceService.checkVncHealth(instanceId, userId);
    final var status = vncReachable ? "HEALTHY" : "UNHEALTHY";
    return new RemoteSessionStatusResponse(status, vncReachable);
  }

  @Nonnull
  private static String currentUserId() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }
}
