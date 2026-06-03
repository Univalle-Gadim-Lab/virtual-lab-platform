package edu.univalle.gadim.virtual_lab_platform.instances.vnc;

import edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceService;
import edu.univalle.gadim.virtual_lab_platform.instances.api.type.Instance;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.security.Principal;
import java.util.concurrent.CompletionStage;
import javax.annotation.ParametersAreNonnullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

/**
 * Proxies WebSocket frames between the browser and a KasmVNC server inside a Docker container.
 *
 * <p>Validates JWT-based authentication and instance ownership before establishing the proxy
 * connection. Forwards binary and text frames bidirectionally between the browser client and
 * the container's KasmVNC WebSocket endpoint.
 */
@ParametersAreNonnullByDefault
public class VncWebSocketProxyHandler extends BinaryWebSocketHandler {

  private static final Logger logger = LoggerFactory.getLogger(VncWebSocketProxyHandler.class);
  private static final String INSTANCE_NOT_RUNNING = "Instance is not in RUNNING status";
  private static final String FORBIDDEN = "User does not own this instance";

  private final InstanceService instanceService;

  public VncWebSocketProxyHandler(InstanceService instanceService) {
    this.instanceService = instanceService;
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession browserSession) throws Exception {
    final var instanceId = extractInstanceId(browserSession);
    final var userId = getUserId(browserSession);

    logger.info("VNC WebSocket connection requested for instance: {} by user: {}",
        instanceId, userId);

    if (!instanceService.checkOwnership(instanceId, userId)) {
      logger.warn("VNC access denied: user {} does not own instance {}", userId, instanceId);
      browserSession.close(new CloseStatus(4003, FORBIDDEN));
      return;
    }

    final var instance = instanceService.getInstanceById(instanceId)
        .orElseThrow(() -> new IllegalArgumentException("Instance not found: " + instanceId));

    if (!"RUNNING".equals(instance.status().name())) {
      logger.warn("VNC access denied: instance {} is not running (status: {})",
          instanceId, instance.status());
      browserSession.close(new CloseStatus(409, INSTANCE_NOT_RUNNING));
      return;
    }

    final var containerUri = URI.create(
        "ws://" + instance.internalIp() + ":" + instance.vncPort() + "/websockify");

    logger.info("Connecting to KasmVNC at: {}", containerUri);

    final var containerWs = connectToContainer(containerUri, browserSession);
    browserSession.getAttributes().put("containerWs", containerWs);
  }

  @Override
  protected void handleBinaryMessage(WebSocketSession browserSession, BinaryMessage message) {
    final var containerWs = (WebSocket) browserSession.getAttributes().get("containerWs");
    if (containerWs != null) {
      containerWs.sendBinary(message.getPayload(), true);
    }
  }

  @Override
  protected void handleTextMessage(WebSocketSession browserSession,
      TextMessage message) {
    final var containerWs = (WebSocket) browserSession.getAttributes().get("containerWs");
    if (containerWs != null) {
      containerWs.sendText(message.getPayload(), true);
    }
  }

  @Override
  public void afterConnectionClosed(WebSocketSession browserSession, CloseStatus status) {
    final var containerWs = (WebSocket) browserSession.getAttributes().get("containerWs");
    if (containerWs != null) {
      containerWs.sendClose(WebSocket.NORMAL_CLOSURE, "browser disconnected");
      logger.debug("VNC container WebSocket closed for session: {}", browserSession.getId());
    }
  }

  @Override
  public void handleTransportError(WebSocketSession browserSession, Throwable exception) {
    logger.error("VNC WebSocket transport error for session: {}", browserSession.getId(),
        exception);
    final var containerWs = (WebSocket) browserSession.getAttributes().get("containerWs");
    if (containerWs != null) {
      containerWs.abort();
    }
  }

  private WebSocket connectToContainer(URI containerUri, WebSocketSession browserSession)
      throws Exception {
    final var httpClient = HttpClient.newHttpClient();
    final var connected = new java.util.concurrent.CompletableFuture<WebSocket>();

    httpClient.newWebSocketBuilder()
        .buildAsync(containerUri, new WebSocket.Listener() {
          final StringBuilder textAccum = new StringBuilder();

          @Override
          public void onOpen(WebSocket webSocket) {
            webSocket.request(Long.MAX_VALUE);
            connected.complete(webSocket);
            logger.debug("Connected to KasmVNC container WebSocket");
          }

          @Override
          public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            textAccum.append(data);
            if (last) {
              try {
                browserSession.sendMessage(
                    new org.springframework.web.socket.TextMessage(textAccum.toString()));
              } catch (Exception e) {
                logger.error("Failed to forward text message to browser", e);
              }
              textAccum.setLength(0);
            }
            return null;
          }

          @Override
          public CompletionStage<?> onBinary(WebSocket webSocket, ByteBuffer data, boolean last) {
            try {
              final var bytes = new byte[data.remaining()];
              data.get(bytes);
              browserSession.sendMessage(new BinaryMessage(bytes, last));
            } catch (Exception e) {
              logger.error("Failed to forward binary message to browser", e);
            }
            return null;
          }

          @Override
          public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            try {
              browserSession.close(new CloseStatus(statusCode, reason));
            } catch (Exception e) {
              logger.error("Failed to close browser WebSocket session", e);
            }
            return null;
          }

          @Override
          public void onError(WebSocket webSocket, Throwable error) {
            logger.error("Container WebSocket error", error);
            try {
              browserSession.close(CloseStatus.SERVER_ERROR);
            } catch (Exception e) {
              logger.error("Failed to close browser session on container error", e);
            }
          }
        })
        .join();

    return connected.get();
  }

  private static String extractInstanceId(WebSocketSession session) {
    final var path = session.getUri().getPath();
    final var segments = path.split("/");
    for (int i = 0; i < segments.length; i++) {
      if ("instances".equals(segments[i]) && i + 1 < segments.length) {
        return segments[i + 1];
      }
    }
    throw new IllegalArgumentException("Could not extract instance ID from path: " + path);
  }

  private static String getUserId(WebSocketSession session) {
    final Principal principal = session.getPrincipal();
    if (principal != null) {
      return principal.getName();
    }
    throw new IllegalArgumentException("No authenticated principal on WebSocket session");
  }
}
