package edu.univalle.gadim.virtual_lab_platform.instances.web.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceStatus;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.CreateInstanceRequest;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.InstanceResponse;
import edu.univalle.gadim.virtual_lab_platform.instances.web.ops.InstancesWsOps;
import java.time.LocalDateTime;
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
@DisplayName("InstanceController")
@ExtendWith(MockitoExtension.class)
class InstanceControllerUnTest {

  private static final String INSTANCE_ID = "inst-001";
  private static final String NAME = "lab-instance";
  private static final String IMAGE_NAME = "lab-kicad";
  private static final String IMAGE_VERSION = "1.0";
  private static final String IMAGE_REGISTRY = "registry.univalle.edu";

  @Mock private InstancesWsOps instancesWsOps;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    final var controller = new InstanceController(instancesWsOps);
    mockMvc = standaloneSetup(controller).build();
    objectMapper = new ObjectMapper().findAndRegisterModules();
  }

  private InstanceResponse buildInstanceResponse() {
    final var now = LocalDateTime.now();
    return new InstanceResponse(
        INSTANCE_ID,
        NAME,
        null,
        IMAGE_NAME,
        IMAGE_VERSION,
        4,
        8192,
        20480,
        true,
        6901,
        true,
        "VncSecret123",
        InstanceStatus.RUNNING,
        now,
        now.plusDays(7),
        now,
        null,
        null,
        null);
  }

  private CreateInstanceRequest buildCreateRequest() {
    return new CreateInstanceRequest(
        NAME,
        null,
        IMAGE_NAME,
        IMAGE_VERSION,
        IMAGE_REGISTRY,
        4,
        8192,
        20480,
        true,
        8080);
  }

  @Nested
  @DisplayName("POST /api/instances")
  class CreateInstance {

    @Test
    @DisplayName("should return instance response with status 200")
    void shouldReturnInstanceResponse() throws Exception {
      // Given
      final var request = buildCreateRequest();
      final var response = buildInstanceResponse();
      when(instancesWsOps.createInstance(any(CreateInstanceRequest.class))).thenReturn(response);

      // When / Then
      mockMvc
          .perform(
              post("/api/instances")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(INSTANCE_ID))
          .andExpect(jsonPath("$.name").value(NAME))
          .andExpect(jsonPath("$.status").value("RUNNING"));
    }
  }

  @Nested
  @DisplayName("GET /api/instances/{id}")
  class GetInstance {

    @Test
    @DisplayName("should return instance when found")
    void shouldReturnInstanceWhenFound() throws Exception {
      // Given
      when(instancesWsOps.getInstanceById(INSTANCE_ID)).thenReturn(buildInstanceResponse());

      // When / Then
      mockMvc
          .perform(get("/api/instances/{id}", INSTANCE_ID))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(INSTANCE_ID));
    }

    @Test
    @DisplayName("should return 404 when not found")
    void shouldReturn404WhenNotFound() throws Exception {
      // Given
      when(instancesWsOps.getInstanceById(INSTANCE_ID))
          .thenThrow(new IllegalArgumentException("not found"));

      // When / Then
      mockMvc.perform(get("/api/instances/{id}", INSTANCE_ID)).andExpect(status().isNotFound());
    }
  }

  @Nested
  @DisplayName("GET /api/instances")
  class ListInstances {

    @Test
    @DisplayName("should return list of instances for current user")
    void shouldReturnListOfInstances() throws Exception {
      // Given
      when(instancesWsOps.getInstancesByUser()).thenReturn(List.of(buildInstanceResponse()));

      // When / Then
      mockMvc
          .perform(get("/api/instances"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.length()").value(1))
          .andExpect(jsonPath("$[0].id").value(INSTANCE_ID));
    }
  }

  @Nested
  @DisplayName("POST /api/instances/{id}/start and /stop")
  class Lifecycle {

    @Test
    @DisplayName("should start instance and return updated response")
    void shouldStartInstance() throws Exception {
      // Given
      when(instancesWsOps.startInstance(INSTANCE_ID)).thenReturn(buildInstanceResponse());

      // When / Then
      mockMvc
          .perform(post("/api/instances/{id}/start", INSTANCE_ID))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(INSTANCE_ID));
    }

    @Test
    @DisplayName("should return 404 when starting a non-existing instance")
    void shouldReturn404WhenStarting() throws Exception {
      // Given
      when(instancesWsOps.startInstance(INSTANCE_ID))
          .thenThrow(new IllegalArgumentException("not found"));

      // When / Then
      mockMvc
          .perform(post("/api/instances/{id}/start", INSTANCE_ID))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should stop instance and return updated response")
    void shouldStopInstance() throws Exception {
      // Given
      when(instancesWsOps.stopInstance(INSTANCE_ID)).thenReturn(buildInstanceResponse());

      // When / Then
      mockMvc
          .perform(post("/api/instances/{id}/stop", INSTANCE_ID))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(INSTANCE_ID));
    }

    @Test
    @DisplayName("should return 404 when stopping a non-existing instance")
    void shouldReturn404WhenStopping() throws Exception {
      // Given
      when(instancesWsOps.stopInstance(INSTANCE_ID))
          .thenThrow(new IllegalArgumentException("not found"));

      // When / Then
      mockMvc
          .perform(post("/api/instances/{id}/stop", INSTANCE_ID))
          .andExpect(status().isNotFound());
    }
  }

  @Nested
  @DisplayName("DELETE /api/instances/{id}")
  class DeleteInstance {

    @Test
    @DisplayName("should return 204 on successful deletion")
    void shouldReturn204OnSuccess() throws Exception {
      // Given
      doNothing().when(instancesWsOps).deleteInstance(INSTANCE_ID);

      // When / Then
      mockMvc.perform(delete("/api/instances/{id}", INSTANCE_ID)).andExpect(status().isNoContent());

      verify(instancesWsOps).deleteInstance(INSTANCE_ID);
    }

    @Test
    @DisplayName("should return 404 when deleting a non-existing instance")
    void shouldReturn404OnMissingInstance() throws Exception {
      // Given
      doThrow(new IllegalArgumentException("not found"))
          .when(instancesWsOps)
          .deleteInstance(INSTANCE_ID);

      // When / Then
      mockMvc.perform(delete("/api/instances/{id}", INSTANCE_ID)).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return 409 when instance is not STOPPED")
    void shouldReturn409OnIllegalState() throws Exception {
      // Given
      doThrow(new IllegalStateException("not stopped"))
          .when(instancesWsOps)
          .deleteInstance(INSTANCE_ID);

      // When / Then
      mockMvc.perform(delete("/api/instances/{id}", INSTANCE_ID)).andExpect(status().isConflict());
    }
  }
}
