package edu.univalle.gadim.virtual_lab_platform.instances.web.operation;

import edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceMetricsService;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.InstanceMetricsResponse;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.RecordMetricsRequest;
import edu.univalle.gadim.virtual_lab_platform.instances.web.ops.InstanceMetricsWsOps;
import java.util.List;
import javax.annotation.Nonnull;
import org.springframework.stereotype.Component;

/**
 * Concrete implementation of {@link InstanceMetricsWsOps} that delegates to the
 * {@link InstanceMetricsService} domain service.
 *
 * <p>This class acts as the bridge between the HTTP contract layer and the
 * business logic layer. It translates incoming request DTOs into domain parameters,
 * invokes the appropriate service methods, and maps the resulting domain objects
 * back into response DTOs suitable for HTTP serialization.
 *
 * @see InstanceMetricsWsOps
 * @see InstanceMetricsService
 */
@Component
public class InstanceMetricsSpringWsOps implements InstanceMetricsWsOps {

  private final InstanceMetricsService instanceMetricsService;

  public InstanceMetricsSpringWsOps(InstanceMetricsService instanceMetricsService) {
    this.instanceMetricsService = instanceMetricsService;
  }

  @Override
  @Nonnull
  public List<InstanceMetricsResponse> getMetricsByInstanceId(@Nonnull String instanceId) {
    return instanceMetricsService.getMetricsByInstanceId(instanceId).stream()
        .map(InstanceMetricsResponse::from)
        .toList();
  }

  @Override
  @Nonnull
  public InstanceMetricsResponse recordMetrics(
      @Nonnull String instanceId,
      @Nonnull RecordMetricsRequest request) {
    final var metrics =
        instanceMetricsService.recordMetrics(
            instanceId,
            request.cpuUsage(),
            request.memoryUsage(),
            request.diskUsage(),
            request.timeUsage());
    return InstanceMetricsResponse.from(metrics);
  }
}
