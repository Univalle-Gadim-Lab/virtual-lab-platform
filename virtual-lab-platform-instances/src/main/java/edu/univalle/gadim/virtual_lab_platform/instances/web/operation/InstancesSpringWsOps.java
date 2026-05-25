package edu.univalle.gadim.virtual_lab_platform.instances.web.operation;

import edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceService;
import edu.univalle.gadim.virtual_lab_platform.instances.api.type.Instance;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.CreateInstanceRequest;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.InstanceResponse;
import edu.univalle.gadim.virtual_lab_platform.instances.web.ops.InstancesWsOps;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.springframework.stereotype.Component;

/**
 * Concrete implementation of {@link InstancesWsOps} that delegates to the
 * {@link InstanceService} domain service.
 *
 * <p>This class acts as the bridge between the HTTP contract layer and the
 * business logic layer. It translates incoming request DTOs into domain parameters,
 * invokes the appropriate service methods, and maps the resulting domain objects
 * back into response DTOs suitable for HTTP serialization.
 *
 * @see InstancesWsOps
 * @see InstanceService
 */
@Component
public class InstancesSpringWsOps implements InstancesWsOps {

  private final InstanceService instanceService;

  public InstancesSpringWsOps(InstanceService instanceService) {
    this.instanceService = instanceService;
  }

  @Override
  @Nonnull
  public InstanceResponse createInstance(@Nonnull CreateInstanceRequest request) {
    final var userId = "current-user-id";
    final var instance =
        instanceService.createInstance(
            userId,
            request.name(),
            Optional.ofNullable(request.description()),
            request.imageName(),
            request.imageVersion(),
            request.imageRegistry(),
            request.cpuCores(),
            request.memoryMb(),
            request.storageMb(),
            request.gpuEnabled(),
            request.exposedPort());
    return InstanceResponse.from(instance);
  }

  @Override
  @Nonnull
  public InstanceResponse getInstanceById(@Nonnull String id) {
    return instanceService
        .getInstanceById(id)
        .map(InstanceResponse::from)
        .orElseThrow(() -> new IllegalArgumentException("Instance not found: " + id));
  }

  @Override
  @Nonnull
  public List<InstanceResponse> getInstancesByUser() {
    final var userId = "current-user-id";
    return instanceService.getInstancesByUserId(userId).stream()
        .map(InstanceResponse::from)
        .toList();
  }

  @Override
  @Nonnull
  public InstanceResponse startInstance(@Nonnull String id) {
    return InstanceResponse.from(instanceService.startInstance(id));
  }

  @Override
  @Nonnull
  public InstanceResponse stopInstance(@Nonnull String id) {
    return InstanceResponse.from(instanceService.stopInstance(id));
  }

  @Override
  public void deleteInstance(@Nonnull String id) {
    instanceService.deleteInstance(id);
  }
}
