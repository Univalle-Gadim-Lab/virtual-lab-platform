package edu.univalle.gadim.virtual_lab_platform.instances.operation;

import edu.univalle.gadim.virtual_lab_platform.commons.type.UniqueIdGenerator;
import edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceMetricsService;
import edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceMetrics;
import edu.univalle.gadim.virtual_lab_platform.instances.data.model.InstanceMetricsJpa;
import edu.univalle.gadim.virtual_lab_platform.instances.data.repository.InstanceMetricsRepository;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for managing instance metrics.
 *
 * <p>This class provides the core business logic for recording and retrieving performance and usage
 * metrics for virtual lab instances.
 */
@Service
@Transactional
@ParametersAreNonnullByDefault
public class InstanceMetricsServiceOperation implements InstanceMetricsService {

  private static final Logger logger =
      LoggerFactory.getLogger(InstanceMetricsServiceOperation.class);

  private final InstanceMetricsRepository instanceMetricsRepository;
  private final UniqueIdGenerator uniqueIdGenerator;

  public InstanceMetricsServiceOperation(
      InstanceMetricsRepository instanceMetricsRepository, UniqueIdGenerator uniqueIdGenerator) {
    this.instanceMetricsRepository = instanceMetricsRepository;
    this.uniqueIdGenerator = uniqueIdGenerator;
  }

  /**
   * Retrieves all recorded metrics for the specified instance.
   *
   * @param instanceId the ID of the instance whose metrics to retrieve
   * @return a list of metrics records, never null but may be empty if no metrics have been recorded
   */
  @Override
  @Nonnull
  @Transactional(readOnly = true)
  public List<InstanceMetrics> getMetricsByInstanceId(String instanceId) {
    logger.debug("Retrieving metrics for instance: {}", instanceId);
    return instanceMetricsRepository.findByInstanceId(instanceId).stream()
        .map(InstanceMetrics.class::cast)
        .toList();
  }

  /**
   * Records a new metrics snapshot for the specified instance.
   *
   * @param instanceId the ID of the instance to record metrics for
   * @param cpuUsage the current CPU usage as a fraction (0.0 to 1.0)
   * @param memoryUsage the current memory usage as a fraction (0.0 to 1.0)
   * @param diskUsage the current disk usage as a fraction (0.0 to 1.0)
   * @param timeUsage the cumulative time usage in seconds
   * @return the persisted metrics record with its generated ID
   */
  @Override
  @Nonnull
  @Transactional
  public InstanceMetrics recordMetrics(
      String instanceId,
      double cpuUsage,
      double memoryUsage,
      double diskUsage,
      double timeUsage) {

    logger.info("Recording metrics for instance: {}", instanceId);

    InstanceMetricsJpa metrics =
        InstanceMetricsJpa.builder()
            .id(uniqueIdGenerator.generate())
            .instanceId(instanceId)
            .currentCpuUsage(cpuUsage)
            .currentMemoryUsage(memoryUsage)
            .currentDiskUsage(diskUsage)
            .currentTimeUsage(timeUsage)
            .build();

    InstanceMetricsJpa savedMetrics = instanceMetricsRepository.save(metrics);
    logger.debug("Metrics recorded successfully with ID: {}", savedMetrics.getId());
    return savedMetrics;
  }
}
