package edu.univalle.gadim.virtual_lab_platform.instances.web.controller;

import edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceService;
import java.net.URI;
import javax.annotation.ParametersAreNonnullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * HTTP reverse proxy for KasmVNC web client assets served from running containers.
 *
 * <p>Proxies HTTP requests from the browser to the KasmVNC web server inside
 * the target container. This enables the KasmVNC web client HTML, JavaScript,
 * CSS, and other static assets to be loaded through the backend, maintaining
 * consistent authentication and avoiding cross-origin issues.
 */
@RestController
@RequestMapping("/api/instances/{instanceId}/vnc")
@ParametersAreNonnullByDefault
public class VncProxyController {

  private static final Logger logger = LoggerFactory.getLogger(VncProxyController.class);

  private final InstanceService instanceService;
  private final RestTemplate restTemplate;

  public VncProxyController(InstanceService instanceService) {
    this.instanceService = instanceService;
    this.restTemplate = new RestTemplate();
  }

  /**
   * Proxies all HTTP GET requests under /vnc/ to the container's KasmVNC web server.
   */
  @GetMapping("/**")
  public ResponseEntity<Resource> proxyVncRequest(@PathVariable String instanceId) {
    final var userId = currentUserId();
    if (!instanceService.checkOwnership(instanceId, userId)) {
      return ResponseEntity.status(403).build();
    }

    final var instance = instanceService.getInstanceById(instanceId)
        .orElseThrow(() -> new IllegalArgumentException("Instance not found: " + instanceId));

    if (!"RUNNING".equals(instance.status().name())) {
      return ResponseEntity.status(409).build();
    }

    final var containerBaseUri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host(instance.internalIp())
        .port(instance.vncPort())
        .build()
        .toUri();

    final var suffix = extractRequestSuffix(instanceId);
    final var targetUrl = URI.create(containerBaseUri.toString() + suffix);

    logger.debug("Proxying VNC HTTP request to: {}", targetUrl);

    try {
      final var response = restTemplate.exchange(
          targetUrl, HttpMethod.GET, HttpEntity.EMPTY, byte[].class);

      final var headers = new HttpHeaders();
      final var contentType = response.getHeaders().getContentType();
      if (contentType != null) {
        headers.setContentType(contentType);
      }

      return ResponseEntity.status(response.getStatusCode())
          .headers(headers)
          .body(new ByteArrayResource(response.getBody()));
    } catch (Exception e) {
      logger.error("Failed to proxy VNC request to: {}", targetUrl, e);
      return ResponseEntity.status(502).build();
    }
  }

  private static String currentUserId() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }

  private String extractRequestSuffix(String instanceId) {
    final var requestAttributes = RequestContextHolder.currentRequestAttributes();
    if (requestAttributes instanceof ServletRequestAttributes sra) {
      final var fullPath = sra.getRequest().getRequestURI();
      final var basePath = "/api/instances/" + instanceId + "/vnc";
      if (fullPath.length() > basePath.length()) {
        return fullPath.substring(basePath.length());
      }
    }
    return "/";
  }
}
