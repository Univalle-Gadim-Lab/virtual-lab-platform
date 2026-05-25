package edu.univalle.gadim.virtual_lab_platform.instances.web.controller;

import edu.univalle.gadim.virtual_lab_platform.instances.web.model.InstanceMetricsResponse;
import edu.univalle.gadim.virtual_lab_platform.instances.web.ops.InstanceMetricsWsOps;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for instance metrics operations.
 *
 * <p>This controller provides endpoints for retrieving performance and usage metrics for virtual
 * lab instances. All operations are delegated to {@link InstanceMetricsWsOps}, keeping this class
 * as a thin HTTP adapter that handles request routing and response status mapping.
 *
 * <h2>Endpoints</h2>
 * <ul>
 *   <li>{@code GET /api/instances/{instanceId}/metrics} — retrieve metrics for an instance</li>
 * </ul>
 *
 * @see InstanceMetricsWsOps
 */
@RestController
@RequestMapping("/api/instances")
@ParametersAreNonnullByDefault
public class InstanceMetricsController {

  private static final Logger logger = LoggerFactory.getLogger(InstanceMetricsController.class);

  private final InstanceMetricsWsOps instanceMetricsWsOps;

  public InstanceMetricsController(InstanceMetricsWsOps instanceMetricsWsOps) {
    this.instanceMetricsWsOps = instanceMetricsWsOps;
  }

  /**
   * Retrieves metrics for a specific instance.
   *
   * @param instanceId the instance ID
   * @return a {@code 200 OK} response with the list of metrics responses
   */
  @GetMapping("/{instanceId}/metrics")
  @Nonnull
  public ResponseEntity<List<InstanceMetricsResponse>> getMetricsByInstanceId(
      @PathVariable String instanceId) {
    logger.debug("Retrieving metrics for instance: {}", instanceId);
    return ResponseEntity.ok(instanceMetricsWsOps.getMetricsByInstanceId(instanceId));
  }
}
