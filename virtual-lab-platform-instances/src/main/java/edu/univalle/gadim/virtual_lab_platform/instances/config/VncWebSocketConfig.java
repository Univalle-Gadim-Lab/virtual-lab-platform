package edu.univalle.gadim.virtual_lab_platform.instances.config;

import edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceService;
import edu.univalle.gadim.virtual_lab_platform.instances.vnc.VncWebSocketProxyHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Registers the VNC WebSocket proxy handler for browser-to-container remote desktop access.
 *
 * <p>WebSocket connections at {@code /api/instances/* /vnc/websockify} are handled by the
 * {@link VncWebSocketProxyHandler}, which validates instance ownership and proxies frames
 * to the corresponding container's KasmVNC server.
 */
@Configuration
@EnableWebSocket
public class VncWebSocketConfig implements WebSocketConfigurer {

  private final InstanceService instanceService;

  public VncWebSocketConfig(InstanceService instanceService) {
    this.instanceService = instanceService;
  }

  @Bean
  public VncWebSocketProxyHandler vncWebSocketProxyHandler() {
    return new VncWebSocketProxyHandler(instanceService);
  }

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(vncWebSocketProxyHandler(), "/api/instances/*/vnc/websockify")
        .setAllowedOriginPatterns("*");
  }
}
