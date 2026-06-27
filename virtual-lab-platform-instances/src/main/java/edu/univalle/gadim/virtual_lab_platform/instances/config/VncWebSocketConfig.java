package edu.univalle.gadim.virtual_lab_platform.instances.config;

import edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceService;
import edu.univalle.gadim.virtual_lab_platform.instances.vnc.VncWebSocketProxyHandler;
import java.util.Map;
import javax.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

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
        .setAllowedOriginPatterns("*")
        .addInterceptors(new HttpSessionHandshakeInterceptor() {
          @Override
          public boolean beforeHandshake(@Nonnull ServerHttpRequest request,
              @Nonnull ServerHttpResponse response,
              @Nonnull WebSocketHandler wsHandler,
              @Nonnull Map<String, Object> attributes) {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
              attributes.put("principal", authentication);
              return true;
            }
            return true;
          }
        });
  }
}
