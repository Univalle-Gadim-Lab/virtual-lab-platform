package edu.univalle.gadim.virtual_lab_platform.instances.web.ops;

import edu.univalle.gadim.virtual_lab_platform.instances.web.model.InstanceMetricsResponse;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.RecordMetricsRequest;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * Web service operations contract for instance metrics management.
 *
 * <p>Defines one method per web endpoint exposed by the instance metrics REST API.
 * Implementations bridge the HTTP layer to the underlying
 * {@link edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceMetricsService}
 * domain service, performing request-to-domain translation and domain-to-response mapping.
 *
 * <h2>Endpoints</h2>
 * <ul>
 *   <li>{@code GET /api/instances/{instanceId}/metrics} — retrieve metrics for an instance</li>
 *   <li>{@code POST /api/instances/{instanceId}/metrics} — record new metrics for an instance</li>
 * </ul>
 */
public interface InstanceMetricsWsOps {

  /**
   * Retrieves metrics for a specific instance.
   *
   * @param instanceId the instance ID to retrieve metrics for
   * @return the list of metrics responses, never null but may be empty
   */
  @Nonnull
  List<InstanceMetricsResponse> getMetricsByInstanceId(@Nonnull String instanceId);

  /**
   * Records new metrics for an instance.
   *
   * @param instanceId the instance ID to record metrics for
   * @param request the record metrics request containing resource utilization data
   * @return the recorded metrics response
   */
  @Nonnull
  InstanceMetricsResponse recordMetrics(
      @Nonnull String instanceId,
      @Nonnull RecordMetricsRequest request);
}
