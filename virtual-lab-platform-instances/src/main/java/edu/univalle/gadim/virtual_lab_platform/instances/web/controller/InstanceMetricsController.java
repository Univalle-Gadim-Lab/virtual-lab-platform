package edu.univalle.gadim.virtual_lab_platform.instances.web.controller;

import edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceMetricsService;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.InstanceMetricsResponse;
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
 * lab instances.
 */
@RestController
@RequestMapping("/api/instances")
@ParametersAreNonnullByDefault
public class InstanceMetricsController {

  private static final Logger logger = LoggerFactory.getLogger(InstanceMetricsController.class);

  private final InstanceMetricsService instanceMetricsService;

  public InstanceMetricsController(InstanceMetricsService instanceMetricsService) {
    this.instanceMetricsService = instanceMetricsService;
  }

  /**
   * Retrieves metrics for a specific instance.
   *
   * @param instanceId the instance ID
   * @return the list of metrics responses
   */
  @GetMapping("/{instanceId}/metrics")
  @Nonnull
  public ResponseEntity<List<InstanceMetricsResponse>> getMetricsByInstanceId(
      @PathVariable String instanceId) {
    logger.debug("Retrieving metrics for instance: {}", instanceId);

    List<InstanceMetricsResponse> responses =
        instanceMetricsService.getMetricsByInstanceId(instanceId).stream()
            .map(InstanceMetricsResponse::from)
            .toList();

    return ResponseEntity.ok(responses);
  }
}
