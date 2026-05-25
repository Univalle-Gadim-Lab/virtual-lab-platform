package edu.univalle.gadim.virtual_lab_platform.instances.data.model;

import static org.assertj.core.api.Assertions.assertThat;

import edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceMetrics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("InstanceMetricsJpa Builder")
class InstanceMetricsJpaBuilderTest {

  private static final String ID = "metrics-001";
  private static final String INSTANCE_ID = "inst-001";
  private static final double CPU_USAGE = 0.45;
  private static final double MEMORY_USAGE = 0.72;
  private static final double DISK_USAGE = 0.33;
  private static final double TIME_USAGE = 120.5;

  private InstanceMetricsJpa.InstanceMetricsJpaBuilder fullBuilder() {
    return InstanceMetricsJpa.builder()
        .id(ID)
        .instanceId(INSTANCE_ID)
        .currentCpuUsage(CPU_USAGE)
        .currentMemoryUsage(MEMORY_USAGE)
        .currentDiskUsage(DISK_USAGE)
        .currentTimeUsage(TIME_USAGE);
  }

  @Nested
  @DisplayName("when building with all fields")
  class AllFields {

    @Test
    @DisplayName("should populate every field via builder getters")
    void shouldPopulateEveryFieldViaGetters() {
      InstanceMetricsJpa metrics = fullBuilder().build();

      assertThat(metrics)
          .returns(ID, InstanceMetricsJpa::getId)
          .returns(INSTANCE_ID, InstanceMetricsJpa::getInstanceId)
          .returns(CPU_USAGE, InstanceMetricsJpa::getCurrentCpuUsage)
          .returns(MEMORY_USAGE, InstanceMetricsJpa::getCurrentMemoryUsage)
          .returns(DISK_USAGE, InstanceMetricsJpa::getCurrentDiskUsage)
          .returns(TIME_USAGE, InstanceMetricsJpa::getCurrentTimeUsage);
    }

    @Test
    @DisplayName("should return correct values from InstanceMetrics interface methods")
    void shouldReturnCorrectValuesFromInterfaceMethods() {
      InstanceMetricsJpa metrics = fullBuilder().build();

      assertThat(metrics)
          .returns(ID, InstanceMetrics::id)
          .returns(INSTANCE_ID, InstanceMetrics::instanceId)
          .returns(CPU_USAGE, InstanceMetrics::currentCpuUsage)
          .returns(MEMORY_USAGE, InstanceMetrics::currentMemoryUsage)
          .returns(DISK_USAGE, InstanceMetrics::currentDiskUsage)
          .returns(TIME_USAGE, InstanceMetrics::currentTimeUsage);
    }

    @Test
    @DisplayName("should implement InstanceMetrics interface")
    void shouldImplementInstanceMetricsInterface() {
      InstanceMetricsJpa metrics = fullBuilder().build();

      assertThat(metrics).isInstanceOf(InstanceMetrics.class);
    }
  }

  @Nested
  @DisplayName("hashCode contract")
  class HashCodeContract {

    @Test
    @DisplayName("should have same hashCode when all fields are the same")
    void shouldHaveSameHashCodeWhenAllFieldsAreTheSame() {
      InstanceMetricsJpa metrics1 =
          InstanceMetricsJpa.builder()
              .id(ID)
              .instanceId(INSTANCE_ID)
              .currentCpuUsage(CPU_USAGE)
              .currentMemoryUsage(MEMORY_USAGE)
              .currentDiskUsage(DISK_USAGE)
              .currentTimeUsage(TIME_USAGE)
              .build();

      InstanceMetricsJpa metrics2 =
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
      InstanceMetricsJpa metrics1 =
          InstanceMetricsJpa.builder()
              .id(ID)
              .instanceId(INSTANCE_ID)
              .currentCpuUsage(CPU_USAGE)
              .currentMemoryUsage(MEMORY_USAGE)
              .currentDiskUsage(DISK_USAGE)
              .currentTimeUsage(TIME_USAGE)
              .build();

      InstanceMetricsJpa metrics2 =
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
      InstanceMetricsJpa metrics = fullBuilder().build();

      assertThat(metrics).isEqualTo(metrics);
    }

    @Test
    @DisplayName("should not be equal to null")
    void shouldNotBeEqualToNull() {
      InstanceMetricsJpa metrics = fullBuilder().build();

      assertThat(metrics).isNotEqualTo(null);
    }

    @Test
    @DisplayName("should not be equal to object of different type")
    void shouldNotBeEqualToDifferentType() {
      InstanceMetricsJpa metrics = fullBuilder().build();

      assertThat(metrics).isNotEqualTo("not-a-metrics");
    }
  }

  @Nested
  @DisplayName("builder instances")
  class BuilderInstances {

    @Test
    @DisplayName("should produce distinct objects on successive builds")
    void shouldProduceDistinctObjectsOnSuccessiveBuilds() {
      InstanceMetricsJpa.InstanceMetricsJpaBuilder builder = fullBuilder();

      InstanceMetricsJpa first = builder.build();
      InstanceMetricsJpa second = builder.build();

      assertThat(first).isNotSameAs(second);
    }

    @Test
    @DisplayName("should allow field override on builder")
    void shouldAllowFieldOverrideOnBuilder() {
      InstanceMetricsJpa metrics =
          InstanceMetricsJpa.builder()
              .id(ID)
              .instanceId(INSTANCE_ID)
              .currentCpuUsage(CPU_USAGE)
              .currentMemoryUsage(MEMORY_USAGE)
              .currentDiskUsage(DISK_USAGE)
              .currentTimeUsage(TIME_USAGE)
              .currentCpuUsage(0.99)
              .build();

      assertThat(metrics)
          .returns(0.99, InstanceMetricsJpa::getCurrentCpuUsage)
          .returns(ID, InstanceMetricsJpa::getId);
    }
  }
}
