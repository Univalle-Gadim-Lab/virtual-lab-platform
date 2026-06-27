package edu.univalle.gadim.virtual_lab_platform.instances.operation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.univalle.gadim.virtual_lab_platform.commons.type.UniqueIdGenerator;
import edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceMetrics;
import edu.univalle.gadim.virtual_lab_platform.instances.data.model.InstanceJpa;
import edu.univalle.gadim.virtual_lab_platform.instances.data.model.InstanceMetricsJpa;
import edu.univalle.gadim.virtual_lab_platform.instances.data.repository.InstanceMetricsRepository;
import edu.univalle.gadim.virtual_lab_platform.instances.data.repository.InstanceRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@NullMarked
@DisplayName("InstanceMetricsServiceOperation")
class InstanceMetricsServiceOperationUnTest {

  private static final String METRICS_ID = "metrics-001";
  private static final String INSTANCE_ID = "inst-001";
  private static final double CPU_USAGE = 0.45;
  private static final double MEMORY_USAGE = 0.72;
  private static final double DISK_USAGE = 0.33;
  private static final double TIME_USAGE = 120.5;

  @Mock private InstanceMetricsRepository instanceMetricsRepository;

  @Mock private InstanceRepository instanceRepository;

  @Mock private UniqueIdGenerator uniqueIdGenerator;

  private InstanceMetricsServiceOperation serviceOperation;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    serviceOperation =
        new InstanceMetricsServiceOperation(
            instanceMetricsRepository, instanceRepository, uniqueIdGenerator);
  }

  private InstanceJpa buildInstance(String id) {
    return InstanceJpa.builder()
        .id(id)
        .name("lab")
        .externalIp("container-001")
        .imageName("lab-kicad")
        .imageVersion("1.0")
        .imageRegistry("registry")
        .cpuCores(4)
        .memoryMb(8192)
        .storageMb(20480)
        .gpuEnabled(true)
        .exposedPort(8080)
        .vncPassword("testPass123")
        .internalIp("127.0.0.1")
        .createdAt(LocalDateTime.now())
        .expiresAt(LocalDateTime.now().plusDays(7))
        .startedAt(LocalDateTime.now())
        .status(edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceStatus.RUNNING)
        .build();
  }

  private InstanceMetricsJpa buildMetrics(String id, String instanceId) {
    return InstanceMetricsJpa.builder()
        .id(id)
        .instanceId(instanceId)
        .currentCpuUsage(CPU_USAGE)
        .currentMemoryUsage(MEMORY_USAGE)
        .currentDiskUsage(DISK_USAGE)
        .currentTimeUsage(TIME_USAGE)
        .build();
  }

  @Nested
  @DisplayName("getMetricsByInstanceId")
  class GetMetricsByInstanceId {

    @Test
    @DisplayName("should return list of metrics for instance")
    void shouldReturnListOfMetrics() {
      // Given
      final var metrics1 = buildMetrics("metrics-001", INSTANCE_ID);
      final var metrics2 = buildMetrics("metrics-002", INSTANCE_ID);
      when(instanceMetricsRepository.findByInstanceId(INSTANCE_ID))
          .thenReturn(List.of(metrics1, metrics2));

      // When
      final var result = serviceOperation.getMetricsByInstanceId(INSTANCE_ID);

      // Then
      assertThat(result).hasSize(2);
      assertThat(result.get(0)).isInstanceOf(InstanceMetrics.class);
    }

    @Test
    @DisplayName("should return empty list when no metrics exist")
    void shouldReturnEmptyListWhenNoneExist() {
      // Given
      when(instanceMetricsRepository.findByInstanceId(INSTANCE_ID)).thenReturn(List.of());

      // When
      final var result = serviceOperation.getMetricsByInstanceId(INSTANCE_ID);

      // Then
      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("recordMetrics")
  class RecordMetrics {

    @Test
    @DisplayName("should generate id, build metrics entity, save, and return saved result")
    void shouldRecordAndReturnMetrics() {
      // Given
      final var instance = buildInstance(INSTANCE_ID);
      final var savedMetrics = buildMetrics(METRICS_ID, INSTANCE_ID);
      when(instanceRepository.findById(INSTANCE_ID)).thenReturn(Optional.of(instance));
      when(uniqueIdGenerator.generate()).thenReturn(METRICS_ID);
      when(instanceMetricsRepository.save(any(InstanceMetricsJpa.class))).thenReturn(savedMetrics);

      // When
      final var result =
          serviceOperation.recordMetrics(
              INSTANCE_ID, CPU_USAGE, MEMORY_USAGE, DISK_USAGE, TIME_USAGE);

      // Then
      assertThat(result).isNotNull();
      assertThat(result.id()).isEqualTo(METRICS_ID);
      assertThat(result.instanceId()).isEqualTo(INSTANCE_ID);
      assertThat(result.currentCpuUsage()).isEqualTo(CPU_USAGE);
      assertThat(result.currentMemoryUsage()).isEqualTo(MEMORY_USAGE);
      assertThat(result.currentDiskUsage()).isEqualTo(DISK_USAGE);
      assertThat(result.currentTimeUsage()).isEqualTo(TIME_USAGE);
      verify(uniqueIdGenerator).generate();
      verify(instanceMetricsRepository).save(any(InstanceMetricsJpa.class));
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when instance does not exist")
    void shouldThrowExceptionWhenInstanceDoesNotExist() {
      // Given
      when(instanceRepository.findById(INSTANCE_ID)).thenReturn(Optional.empty());

      // When / Then
      assertThatThrownBy(
              () ->
                  serviceOperation.recordMetrics(
                      INSTANCE_ID, CPU_USAGE, MEMORY_USAGE, DISK_USAGE, TIME_USAGE))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining(INSTANCE_ID);
    }
  }
}
