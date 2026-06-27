package edu.univalle.gadim.virtual_lab_platform.instances.web.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.springframework.http.MediaType;

/**
 * Response DTO for VNC HTTP proxy operations.
 *
 * <p>Encapsulates the proxied response body, content type, and HTTP status
 * returned by the KasmVNC web server inside the target container.
 */
public record VncProxyResponse(
    int statusCode,
    @Nullable MediaType contentType,
    @Nullable byte[] body) {

  /**
   * Creates an error response with the given HTTP status code and no body.
   *
   * @param statusCode the HTTP error status code
   * @return a VNC proxy response representing the error
   */
  @Nonnull
  public static VncProxyResponse error(int statusCode) {
    return new VncProxyResponse(statusCode, null, null);
  }
}
