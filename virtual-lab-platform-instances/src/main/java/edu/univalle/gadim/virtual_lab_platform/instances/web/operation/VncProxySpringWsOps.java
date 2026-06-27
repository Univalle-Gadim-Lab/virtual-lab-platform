package edu.univalle.gadim.virtual_lab_platform.instances.web.operation;

import edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceService;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.VncProxyResponse;
import edu.univalle.gadim.virtual_lab_platform.instances.web.ops.VncProxyWsOps;
import java.net.URI;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Concrete implementation of {@link VncProxyWsOps} that delegates to
 * {@link InstanceService} for VNC HTTP proxy operations.
 *
 * <p>Bridges the HTTP contract layer to the business logic layer, performing
 * ownership verification, instance state validation, and HTTP request proxying
 * to the KasmVNC web server inside the target container.
 *
 * @see VncProxyWsOps
 * @see InstanceService
 */
@Component
public class VncProxySpringWsOps implements VncProxyWsOps {

  private static final Logger logger = LoggerFactory.getLogger(VncProxySpringWsOps.class);

  private final InstanceService instanceService;
  private final RestTemplate restTemplate;

  public VncProxySpringWsOps(InstanceService instanceService) {
    this.instanceService = instanceService;
    this.restTemplate = new RestTemplate();
  }

  @Override
  @Nonnull
  public VncProxyResponse proxyVncRequest(
      @Nonnull String instanceId, @Nonnull String requestSuffix) {
    final var userId = currentUserId();
    if (!instanceService.checkOwnership(instanceId, userId)) {
      return VncProxyResponse.error(403);
    }

    final var instance = instanceService.getInstanceById(instanceId)
        .orElseThrow(() -> new IllegalArgumentException("Instance not found: " + instanceId));

    if (!"RUNNING".equals(instance.status().name())) {
      return VncProxyResponse.error(409);
    }

    final var containerBaseUri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(instance.vncPort())
        .build()
        .toUri();

    final var targetUrl = URI.create(containerBaseUri.toString() + requestSuffix);

    logger.debug("Proxying VNC HTTP request to: {}", targetUrl);

    try {
      final var requestHeaders = new HttpHeaders();
      requestHeaders.setBasicAuth("labuser", instance.vncPassword());
      final var requestEntity = new HttpEntity<>(requestHeaders);
      final var containerResponse = restTemplate.exchange(
          targetUrl, HttpMethod.GET, requestEntity, byte[].class);

      return new VncProxyResponse(
          containerResponse.getStatusCode().value(),
          containerResponse.getHeaders().getContentType(),
          containerResponse.getBody());
    } catch (Exception e) {
      logger.error("Failed to proxy VNC request to: {}", targetUrl, e);
      return VncProxyResponse.error(502);
    }
  }

  @Nonnull
  private static String currentUserId() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }
}
