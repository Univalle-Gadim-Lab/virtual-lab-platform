package edu.univalle.gadim.virtual_lab_platform.instances.web.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import edu.univalle.gadim.virtual_lab_platform.instances.web.model.VncProxyResponse;
import edu.univalle.gadim.virtual_lab_platform.instances.web.ops.VncProxyWsOps;
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
@DisplayName("VncProxyController")
@ExtendWith(MockitoExtension.class)
class VncProxyControllerUnTest {

  private static final String INSTANCE_ID = "inst-001";

  @Mock private VncProxyWsOps vncProxyWsOps;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    final var controller = new VncProxyController(vncProxyWsOps);
    mockMvc = standaloneSetup(controller).build();
  }

  @Nested
  @DisplayName("GET /api/instances/{instanceId}/vnc/**")
  class Proxy {

    @Test
    @DisplayName("should return proxied body with the upstream content type")
    void shouldReturnProxiedBody() throws Exception {
      // Given
      final var body = "<html>client</html>".getBytes();
      when(vncProxyWsOps.proxyVncRequest(eq(INSTANCE_ID), any()))
          .thenReturn(new VncProxyResponse(200, MediaType.TEXT_HTML, body));

      // When / Then
      mockMvc
          .perform(get("/api/instances/{instanceId}/vnc/index.html", INSTANCE_ID))
          .andExpect(status().isOk())
          .andExpect(header().string("Content-Type", "text/html"))
          .andExpect(content().bytes(body));
    }

    @Test
    @DisplayName("should return 403 when caller does not own the instance")
    void shouldReturn403() throws Exception {
      // Given
      when(vncProxyWsOps.proxyVncRequest(eq(INSTANCE_ID), any()))
          .thenReturn(VncProxyResponse.error(403));

      // When / Then
      mockMvc
          .perform(get("/api/instances/{instanceId}/vnc/index.html", INSTANCE_ID))
          .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("should return 409 when instance is not in RUNNING status")
    void shouldReturn409() throws Exception {
      // Given
      when(vncProxyWsOps.proxyVncRequest(eq(INSTANCE_ID), any()))
          .thenReturn(VncProxyResponse.error(409));

      // When / Then
      mockMvc
          .perform(get("/api/instances/{instanceId}/vnc/index.html", INSTANCE_ID))
          .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("should propagate empty body and content type from WsOps")
    void shouldPropagateEmptyBody() throws Exception {
      // Given
      when(vncProxyWsOps.proxyVncRequest(eq(INSTANCE_ID), any()))
          .thenReturn(new VncProxyResponse(204, null, null));

      // When / Then
      mockMvc
          .perform(get("/api/instances/{instanceId}/vnc/empty", INSTANCE_ID))
          .andExpect(status().isNoContent());
    }
  }

  @Nested
  @DisplayName("vnc_token cookie handling")
  class CookieHandling {

    @Test
    @DisplayName("should mirror the ?token= query parameter into a vnc_token cookie")
    void shouldSetCookieFromQueryToken() throws Exception {
      // Given
      when(vncProxyWsOps.proxyVncRequest(eq(INSTANCE_ID), any()))
          .thenReturn(new VncProxyResponse(200, MediaType.TEXT_HTML, new byte[0]));

      // When / Then
      mockMvc
          .perform(
              get("/api/instances/{instanceId}/vnc/index.html", INSTANCE_ID).param("token", "jwt-abc"))
          .andExpect(status().isOk())
          .andExpect(cookie().value("vnc_token", "jwt-abc"));

      verify(vncProxyWsOps).proxyVncRequest(eq(INSTANCE_ID), any());
    }

    @Test
    @DisplayName("should not overwrite an existing vnc_token cookie")
    void shouldNotOverwriteExistingCookie() throws Exception {
      // Given
      when(vncProxyWsOps.proxyVncRequest(eq(INSTANCE_ID), any()))
          .thenReturn(new VncProxyResponse(200, MediaType.TEXT_HTML, new byte[0]));

      // When / Then — pre-existing cookie from the browser must be preserved
      mockMvc
          .perform(
              get("/api/instances/{instanceId}/vnc/index.html", INSTANCE_ID)
                  .cookie(new jakarta.servlet.http.Cookie("vnc_token", "existing-jwt")))
          .andExpect(status().isOk());

      verify(vncProxyWsOps).proxyVncRequest(eq(INSTANCE_ID), any());
    }
  }
}
