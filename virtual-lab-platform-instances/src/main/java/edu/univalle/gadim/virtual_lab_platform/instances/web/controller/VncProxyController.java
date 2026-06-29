package edu.univalle.gadim.virtual_lab_platform.instances.web.controller;

import edu.univalle.gadim.virtual_lab_platform.instances.web.model.VncProxyResponse;
import edu.univalle.gadim.virtual_lab_platform.instances.web.ops.VncProxyWsOps;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * HTTP reverse proxy for KasmVNC web client assets served from running containers.
 *
 * <p>Proxies HTTP requests from the browser to the KasmVNC web server inside
 * the target container. This enables the KasmVNC web client HTML, JavaScript,
 * CSS, and other static assets to be loaded through the backend, maintaining
 * consistent authentication and avoiding cross-origin issues.
 *
 * <p>All business logic (ownership verification, instance state validation,
 * HTTP request proxying) is delegated to {@link VncProxyWsOps}, keeping this
 * class as a thin HTTP adapter that handles request routing, cookie management,
 * and response status mapping.
 *
 * @see VncProxyWsOps
 */
@RestController
@RequestMapping("/api/instances/{instanceId}/vnc")
@ParametersAreNonnullByDefault
public class VncProxyController {

  private static final Logger logger = LoggerFactory.getLogger(VncProxyController.class);

  private final VncProxyWsOps vncProxyWsOps;

  public VncProxyController(VncProxyWsOps vncProxyWsOps) {
    this.vncProxyWsOps = vncProxyWsOps;
  }

  /**
   * Proxies all HTTP GET requests under /vnc/ to the container's KasmVNC web server.
   */
  @GetMapping(value = "/**", headers = "!Upgrade")
  @Nonnull
  public ResponseEntity<Resource> proxyVncRequest(
      @PathVariable String instanceId,
      HttpServletRequest request,
      HttpServletResponse response) {
    setVncTokenCookie(instanceId, request, response);

    final var suffix = extractRequestSuffix(instanceId);
    final var proxyResponse = vncProxyWsOps.proxyVncRequest(instanceId, suffix);

    if (proxyResponse.body() == null) {
      return ResponseEntity.status(proxyResponse.statusCode()).build();
    }

    final var headers = new HttpHeaders();
    if (proxyResponse.contentType() != null) {
      headers.setContentType(proxyResponse.contentType());
    }

    return ResponseEntity.status(proxyResponse.statusCode())
        .headers(headers)
        .body(new ByteArrayResource(proxyResponse.body()));
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

  private static void setVncTokenCookie(
      String instanceId, HttpServletRequest request, HttpServletResponse response) {
    var token = request.getParameter("token");
    if (token == null || token.isEmpty()) {
      var cookies = request.getCookies();
      if (cookies != null) {
        for (var cookie : cookies) {
          if ("vnc_token".equals(cookie.getName())) {
            return;
          }
        }
      }
      return;
    }
    var cookie = new Cookie("vnc_token", token);
    cookie.setHttpOnly(true);
    cookie.setPath("/api/instances/" + instanceId + "/vnc");
    cookie.setMaxAge(3600);
    response.addCookie(cookie);
  }
}
