package edu.univalle.gadim.virtual_lab_platform.instances.web.operation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceMetricsService;
import edu.univalle.gadim.virtual_lab_platform.instances.data.model.InstanceMetricsJpa;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.RecordMetricsRequest;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@NullMarked
@DisplayName("InstanceMetricsSpringWsOps")
@ExtendWith(MockitoExtension.class)
class InstanceMetricsSpringWsOpsUnTest {

  private static final String INSTANCE_ID = "inst-001";

  @Mock private InstanceMetricsService instanceMetricsService;

  private InstanceMetricsSpringWsOps wsOps;

  @BeforeEach
  void setUp() {
    wsOps = new InstanceMetricsSpringWsOps(instanceMetricsService);
  }

  private InstanceMetricsJpa buildMetrics() {
    return InstanceMetricsJpa.builder()
        .id("metrics-1")
        .instanceId(INSTANCE_ID)
        .currentCpuUsage(0.42)
        .currentMemoryUsage(0.55)
        .currentDiskUsage(0.10)
        .currentTimeUsage(1200.0)
        .build();
  }

  @Test
  @DisplayName("should map service metrics to response DTOs")
  void shouldMapMetrics() {
    // Given
    when(instanceMetricsService.getMetricsByInstanceId(INSTANCE_ID))
        .thenReturn(List.of(buildMetrics()));

    // When
    final var response = wsOps.getMetricsByInstanceId(INSTANCE_ID);

    // Then
    assertThat(response)
        .hasSize(1)
        .first()
        .returns("metrics-1", edu.univalle.gadim.virtual_lab_platform.instances.web.model.InstanceMetricsResponse::id)
        .returns(0.42, edu.univalle.gadim.virtual_lab_platform.instances.web.model.InstanceMetricsResponse::currentCpuUsage);
  }

  @Test
  @DisplayName("should translate RecordMetricsRequest into service parameters")
  void shouldTranslateRequest() {
    // Given
    final var request = new RecordMetricsRequest(0.42, 0.55, 0.10, 1200.0);
    when(instanceMetricsService.recordMetrics(INSTANCE_ID, 0.42, 0.55, 0.10, 1200.0))
        .thenReturn(buildMetrics());

    // When
    final var response = wsOps.recordMetrics(INSTANCE_ID, request);

    // Then
    assertThat(response.id()).isEqualTo("metrics-1");
    assertThat(response.instanceId()).isEqualTo(INSTANCE_ID);
  }
}
