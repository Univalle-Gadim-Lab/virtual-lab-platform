package edu.univalle.gadim.virtual_lab_platform.instances.operation;

import edu.univalle.gadim.virtual_lab_platform.commons.type.UniqueIdGenerator;
import edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceMetricsService;
import edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceMetrics;
import edu.univalle.gadim.virtual_lab_platform.instances.data.model.InstanceMetricsJpa;
import edu.univalle.gadim.virtual_lab_platform.instances.data.repository.InstanceMetricsRepository;
import edu.univalle.gadim.virtual_lab_platform.instances.data.repository.InstanceRepository;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@ParametersAreNonnullByDefault
public class InstanceMetricsServiceOperation implements InstanceMetricsService {

  private static final Logger logger =
      LoggerFactory.getLogger(InstanceMetricsServiceOperation.class);
  private static final String INSTANCE_NOT_FOUND = "Instance not found: ";

  private final InstanceMetricsRepository instanceMetricsRepository;
  private final InstanceRepository instanceRepository;
  private final UniqueIdGenerator uniqueIdGenerator;

  public InstanceMetricsServiceOperation(
      InstanceMetricsRepository instanceMetricsRepository,
      InstanceRepository instanceRepository,
      UniqueIdGenerator uniqueIdGenerator) {
    this.instanceMetricsRepository = instanceMetricsRepository;
    this.instanceRepository = instanceRepository;
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

    instanceRepository
        .findById(instanceId)
        .orElseThrow(() -> new IllegalArgumentException(INSTANCE_NOT_FOUND + instanceId));

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
