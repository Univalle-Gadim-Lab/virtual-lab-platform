package edu.univalle.gadim.virtual_lab_platform.instances.data.model;

import static org.assertj.core.api.Assertions.assertThat;

import edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceMetrics;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@NullMarked
@DisplayName("InstanceMetricsJpa")
class InstanceMetricsJpaUnTest {

  private static final String ID = "metrics-001";
  private static final String INSTANCE_ID = "inst-001";
  private static final Double CPU_USAGE = 0.45;
  private static final Double MEMORY_USAGE = 0.72;
  private static final Double DISK_USAGE = 0.33;
  private static final Double TIME_USAGE = 120.5;

  @Nested
  @DisplayName("no-args constructor")
  class NoArgsConstructor {

    @Test
    @DisplayName("should create instance with null fields")
    void shouldCreateInstanceWithNullFields() {
      final var metrics = new InstanceMetricsJpa();

      assertThat(metrics.getId()).isNull();
      assertThat(metrics.getInstanceId()).isNull();
      assertThat(metrics.getCurrentCpuUsage()).isNull();
      assertThat(metrics.getCurrentMemoryUsage()).isNull();
      assertThat(metrics.getCurrentDiskUsage()).isNull();
      assertThat(metrics.getCurrentTimeUsage()).isNull();
    }
  }

  @Nested
  @DisplayName("all-args constructor")
  class AllArgsConstructor {

    @Test
    @DisplayName("should populate all fields")
    void shouldPopulateAllFields() {
      final var metrics =
          new InstanceMetricsJpa(ID, INSTANCE_ID, CPU_USAGE, MEMORY_USAGE, DISK_USAGE, TIME_USAGE);

      assertThat(metrics)
          .returns(ID, InstanceMetricsJpa::getId)
          .returns(INSTANCE_ID, InstanceMetricsJpa::getInstanceId)
          .returns(CPU_USAGE, InstanceMetricsJpa::getCurrentCpuUsage)
          .returns(MEMORY_USAGE, InstanceMetricsJpa::getCurrentMemoryUsage)
          .returns(DISK_USAGE, InstanceMetricsJpa::getCurrentDiskUsage)
          .returns(TIME_USAGE, InstanceMetricsJpa::getCurrentTimeUsage);
    }
  }

  @Nested
  @DisplayName("setters")
  class Setters {

    @Test
    @DisplayName("should update id")
    void shouldUpdateId() {
      final var metrics = new InstanceMetricsJpa();
      metrics.setId(ID);

      assertThat(metrics.getId()).isEqualTo(ID);
    }

    @Test
    @DisplayName("should update instanceId")
    void shouldUpdateInstanceId() {
      final var metrics = new InstanceMetricsJpa();
      metrics.setInstanceId(INSTANCE_ID);

      assertThat(metrics.getInstanceId()).isEqualTo(INSTANCE_ID);
    }

    @Test
    @DisplayName("should update currentCpuUsage")
    void shouldUpdateCurrentCpuUsage() {
      final var metrics = new InstanceMetricsJpa();
      metrics.setCurrentCpuUsage(CPU_USAGE);

      assertThat(metrics.getCurrentCpuUsage()).isEqualTo(CPU_USAGE);
    }

    @Test
    @DisplayName("should update currentMemoryUsage")
    void shouldUpdateCurrentMemoryUsage() {
      final var metrics = new InstanceMetricsJpa();
      metrics.setCurrentMemoryUsage(MEMORY_USAGE);

      assertThat(metrics.getCurrentMemoryUsage()).isEqualTo(MEMORY_USAGE);
    }

    @Test
    @DisplayName("should update currentDiskUsage")
    void shouldUpdateCurrentDiskUsage() {
      final var metrics = new InstanceMetricsJpa();
      metrics.setCurrentDiskUsage(DISK_USAGE);

      assertThat(metrics.getCurrentDiskUsage()).isEqualTo(DISK_USAGE);
    }

    @Test
    @DisplayName("should update currentTimeUsage")
    void shouldUpdateCurrentTimeUsage() {
      final var metrics = new InstanceMetricsJpa();
      metrics.setCurrentTimeUsage(TIME_USAGE);

      assertThat(metrics.getCurrentTimeUsage()).isEqualTo(TIME_USAGE);
    }
  }

  @Nested
  @DisplayName("toString")
  class ToString {

    @Test
    @DisplayName("should contain class name and field values")
    void shouldContainClassNameAndFieldValues() {
      final var metrics =
          InstanceMetricsJpa.builder()
              .id(ID)
              .instanceId(INSTANCE_ID)
              .currentCpuUsage(CPU_USAGE)
              .currentMemoryUsage(MEMORY_USAGE)
              .currentDiskUsage(DISK_USAGE)
              .currentTimeUsage(TIME_USAGE)
              .build();

      final var result = metrics.toString();

      assertThat(result)
          .contains("InstanceMetricsJpa")
          .contains(ID)
          .contains(INSTANCE_ID)
          .contains(String.valueOf(CPU_USAGE))
          .contains(String.valueOf(MEMORY_USAGE));
    }
  }

  @Nested
  @DisplayName("interface methods")
  class InterfaceMethods {

    @Test
    @DisplayName("should implement InstanceMetrics interface")
    void shouldImplementInstanceMetricsInterface() {
      final var metrics =
          InstanceMetricsJpa.builder()
              .id(ID)
              .instanceId(INSTANCE_ID)
              .currentCpuUsage(CPU_USAGE)
              .currentMemoryUsage(MEMORY_USAGE)
              .currentDiskUsage(DISK_USAGE)
              .currentTimeUsage(TIME_USAGE)
              .build();

      assertThat(metrics).isInstanceOf(InstanceMetrics.class);
    }

    @Test
    @DisplayName("should return correct values from interface methods")
    void shouldReturnCorrectValuesFromInterfaceMethods() {
      final var metrics =
          InstanceMetricsJpa.builder()
              .id(ID)
              .instanceId(INSTANCE_ID)
              .currentCpuUsage(CPU_USAGE)
              .currentMemoryUsage(MEMORY_USAGE)
              .currentDiskUsage(DISK_USAGE)
              .currentTimeUsage(TIME_USAGE)
              .build();

      assertThat(metrics)
          .returns(ID, InstanceMetrics::id)
          .returns(INSTANCE_ID, InstanceMetrics::instanceId)
          .returns(CPU_USAGE, InstanceMetrics::currentCpuUsage)
          .returns(MEMORY_USAGE, InstanceMetrics::currentMemoryUsage)
          .returns(DISK_USAGE, InstanceMetrics::currentDiskUsage)
          .returns(TIME_USAGE, InstanceMetrics::currentTimeUsage);
    }
  }

  @Nested
  @DisplayName("hashCode")
  class HashCode {

    @Test
    @DisplayName("should have same hashCode when all fields are the same")
    void shouldHaveSameHashCodeWhenAllFieldsAreTheSame() {
      final var metrics1 =
          InstanceMetricsJpa.builder()
              .id(ID)
              .instanceId(INSTANCE_ID)
              .currentCpuUsage(CPU_USAGE)
              .currentMemoryUsage(MEMORY_USAGE)
              .currentDiskUsage(DISK_USAGE)
              .currentTimeUsage(TIME_USAGE)
              .build();

      final var metrics2 =
          InstanceMetricsJpa.builder()
              .id(ID)
              .instanceId(INSTANCE_ID)
              .currentCpuUsage(CPU_USAGE)
              .currentMemoryUsage(MEMORY_USAGE)
              .currentDiskUsage(DISK_USAGE)
              .currentTimeUsage(TIME_USAGE)
              .build();

      assertThat(metrics1).hasSameHashCodeAs(metrics2);
    }

    @Test
    @DisplayName("should be equal when all fields are the same")
    void shouldBeEqualWhenAllFieldsAreTheSame() {
      final var metrics1 =
          InstanceMetricsJpa.builder()
              .id(ID)
              .instanceId(INSTANCE_ID)
              .currentCpuUsage(CPU_USAGE)
              .currentMemoryUsage(MEMORY_USAGE)
              .currentDiskUsage(DISK_USAGE)
              .currentTimeUsage(TIME_USAGE)
              .build();

      final var metrics2 =
          InstanceMetricsJpa.builder()
              .id(ID)
              .instanceId(INSTANCE_ID)
              .currentCpuUsage(CPU_USAGE)
              .currentMemoryUsage(MEMORY_USAGE)
              .currentDiskUsage(DISK_USAGE)
              .currentTimeUsage(TIME_USAGE)
              .build();

      assertThat(metrics1).isEqualTo(metrics2);
    }

    @Test
    @DisplayName("should be equal to itself")
    void shouldBeEqualToItself() {
      final var metrics =
          InstanceMetricsJpa.builder()
              .id(ID)
              .instanceId(INSTANCE_ID)
              .currentCpuUsage(CPU_USAGE)
              .build();

      assertThat(metrics).isEqualTo(metrics);
    }

    @Test
    @DisplayName("should not be equal to null")
    void shouldNotBeEqualToNull() {
      final var metrics =
          InstanceMetricsJpa.builder()
              .id(ID)
              .instanceId(INSTANCE_ID)
              .currentCpuUsage(CPU_USAGE)
              .build();

      assertThat(metrics).isNotEqualTo(null);
    }

    @Test
    @DisplayName("should not be equal to object of different type")
    void shouldNotBeEqualToDifferentType() {
      final var metrics =
          InstanceMetricsJpa.builder()
              .id(ID)
              .instanceId(INSTANCE_ID)
              .currentCpuUsage(CPU_USAGE)
              .build();

      assertThat(metrics).isNotEqualTo("not-a-metrics");
    }
  }
}
