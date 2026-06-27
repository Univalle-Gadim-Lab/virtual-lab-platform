package edu.univalle.gadim.virtual_lab_platform.instances.web.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.InstanceMetricsResponse;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.RecordMetricsRequest;
import edu.univalle.gadim.virtual_lab_platform.instances.web.ops.InstanceMetricsWsOps;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@NullMarked
@DisplayName("InstanceMetricsController")
@ExtendWith(MockitoExtension.class)
class InstanceMetricsControllerUnTest {

  private static final String INSTANCE_ID = "inst-001";

  @Mock private InstanceMetricsWsOps instanceMetricsWsOps;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    final var controller = new InstanceMetricsController(instanceMetricsWsOps);
    mockMvc = standaloneSetup(controller).build();
    objectMapper = new ObjectMapper().findAndRegisterModules();
  }

  private InstanceMetricsResponse buildMetrics() {
    return new InstanceMetricsResponse("metrics-1", INSTANCE_ID, 0.42, 0.55, 0.10, 1200.0);
  }

  @Nested
  @DisplayName("GET /api/instances/{instanceId}/metrics")
  class GetMetrics {

    @Test
    @DisplayName("should return list of metrics for the given instance")
    void shouldReturnMetricsList() throws Exception {
      // Given
      when(instanceMetricsWsOps.getMetricsByInstanceId(INSTANCE_ID))
          .thenReturn(List.of(buildMetrics()));

      // When / Then
      mockMvc
          .perform(get("/api/instances/{instanceId}/metrics", INSTANCE_ID))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.length()").value(1))
          .andExpect(jsonPath("$[0].id").value("metrics-1"))
          .andExpect(jsonPath("$[0].currentCpuUsage").value(0.42));
    }
  }

  @Nested
  @DisplayName("POST /api/instances/{instanceId}/metrics")
  class RecordMetrics {

    @Test
    @DisplayName("should return 200 with recorded metrics on success")
    void shouldReturnRecordedMetrics() throws Exception {
      // Given
      final var request = new RecordMetricsRequest(0.42, 0.55, 0.10, 1200.0);
      when(instanceMetricsWsOps.recordMetrics(any(), any(RecordMetricsRequest.class)))
          .thenReturn(buildMetrics());

      // When / Then
      mockMvc
          .perform(
              post("/api/instances/{instanceId}/metrics", INSTANCE_ID)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value("metrics-1"));
    }

    @Test
    @DisplayName("should return 404 when instance does not exist")
    void shouldReturn404WhenInstanceMissing() throws Exception {
      // Given
      final var request = new RecordMetricsRequest(0.42, 0.55, 0.10, 1200.0);
      when(instanceMetricsWsOps.recordMetrics(any(), any(RecordMetricsRequest.class)))
          .thenThrow(new IllegalArgumentException("not found"));

      // When / Then
      mockMvc
          .perform(
              post("/api/instances/{instanceId}/metrics", INSTANCE_ID)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isNotFound());
    }
  }
}
