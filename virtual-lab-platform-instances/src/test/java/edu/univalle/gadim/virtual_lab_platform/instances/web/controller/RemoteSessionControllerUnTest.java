package edu.univalle.gadim.virtual_lab_platform.instances.web.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import edu.univalle.gadim.virtual_lab_platform.instances.web.model.RemoteSessionResponse;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.RemoteSessionStatusResponse;
import edu.univalle.gadim.virtual_lab_platform.instances.web.ops.RemoteSessionWsOps;
import java.time.LocalDateTime;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

@NullMarked
@DisplayName("RemoteSessionController")
@ExtendWith(MockitoExtension.class)
class RemoteSessionControllerUnTest {

  private static final String INSTANCE_ID = "inst-001";

  @Mock private RemoteSessionWsOps remoteSessionWsOps;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    final var controller = new RemoteSessionController(remoteSessionWsOps);
    mockMvc = standaloneSetup(controller).build();
  }

  private RemoteSessionResponse buildSession() {
    return new RemoteSessionResponse(
        INSTANCE_ID, "ACTIVE", true, "http://localhost:6901?password=abc", LocalDateTime.now().plusHours(1));
  }

  @Nested
  @DisplayName("GET /api/instances/{instanceId}/remote-session")
  class GetSession {

    @Test
    @DisplayName("should return 200 with session metadata when found")
    void shouldReturnSession() throws Exception {
      // Given
      when(remoteSessionWsOps.getRemoteSession(INSTANCE_ID)).thenReturn(buildSession());

      // When / Then
      mockMvc
          .perform(get("/api/instances/{instanceId}/remote-session", INSTANCE_ID))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.instanceId").value(INSTANCE_ID))
          .andExpect(jsonPath("$.status").value("ACTIVE"))
          .andExpect(jsonPath("$.vncEnabled").value(true));
    }

    @Test
    @DisplayName("should return 404 when instance does not exist")
    void shouldReturn404WhenMissing() throws Exception {
      // Given
      when(remoteSessionWsOps.getRemoteSession(INSTANCE_ID))
          .thenThrow(new IllegalArgumentException("not found"));

      // When / Then
      mockMvc
          .perform(get("/api/instances/{instanceId}/remote-session", INSTANCE_ID))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return 403 when caller does not own the instance")
    void shouldReturn403WhenForbidden() throws Exception {
      // Given
      when(remoteSessionWsOps.getRemoteSession(INSTANCE_ID))
          .thenThrow(new SecurityException("forbidden"));

      // When / Then
      mockMvc
          .perform(get("/api/instances/{instanceId}/remote-session", INSTANCE_ID))
          .andExpect(status().isForbidden());
    }
  }

  @Nested
  @DisplayName("DELETE /api/instances/{instanceId}/remote-session")
  class TerminateSession {

    @Test
    @DisplayName("should return 204 on successful termination")
    void shouldReturn204OnSuccess() throws Exception {
      // Given
      doNothing().when(remoteSessionWsOps).terminateRemoteSession(INSTANCE_ID);

      // When / Then
      mockMvc
          .perform(delete("/api/instances/{instanceId}/remote-session", INSTANCE_ID))
          .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("should return 404 when instance does not exist")
    void shouldReturn404WhenMissing() throws Exception {
      // Given
      doThrow(new IllegalArgumentException("not found"))
          .when(remoteSessionWsOps)
          .terminateRemoteSession(INSTANCE_ID);

      // When / Then
      mockMvc
          .perform(delete("/api/instances/{instanceId}/remote-session", INSTANCE_ID))
          .andExpect(status().isNotFound());
    }
  }

  @Nested
  @DisplayName("GET /api/instances/{instanceId}/remote-session/status")
  class SessionStatus {

    @Test
    @DisplayName("should return 200 with the reachability status")
    void shouldReturnStatus() throws Exception {
      // Given
      when(remoteSessionWsOps.getRemoteSessionStatus(INSTANCE_ID))
          .thenReturn(new RemoteSessionStatusResponse("UP", true));

      // When / Then
      mockMvc
          .perform(get("/api/instances/{instanceId}/remote-session/status", INSTANCE_ID))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.status").value("UP"))
          .andExpect(jsonPath("$.vncReachable").value(true));
    }

    @Test
    @DisplayName("should return 404 when instance does not exist")
    void shouldReturn404WhenMissing() throws Exception {
      // Given
      when(remoteSessionWsOps.getRemoteSessionStatus(INSTANCE_ID))
          .thenThrow(new IllegalArgumentException("not found"));

      // When / Then
      mockMvc
          .perform(get("/api/instances/{instanceId}/remote-session/status", INSTANCE_ID))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return 403 when caller does not own the instance")
    void shouldReturn403WhenForbidden() throws Exception {
      // Given
      when(remoteSessionWsOps.getRemoteSessionStatus(INSTANCE_ID))
          .thenThrow(new SecurityException("forbidden"));

      // When / Then
      mockMvc
          .perform(get("/api/instances/{instanceId}/remote-session/status", INSTANCE_ID))
          .andExpect(status().isForbidden());
    }
  }
}
