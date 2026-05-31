package edu.univalle.gadim.virtual_lab_platform.boot.web.controller;

import edu.univalle.gadim.virtual_lab_platform.boot.web.model.HealthResponse;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for application health checks.
 *
 * <p>Provides an unauthenticated endpoint for load balancers and monitoring
 * probes to verify that the application is running and accepting requests.
 *
 * <h2>Endpoints</h2>
 * <ul>
 *   <li>{@code GET /api/health} — returns application status</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/health")
@ParametersAreNonnullByDefault
public class HealthController {

  /**
   * Returns the current application health status.
   *
   * @return a {@code 200 OK} response with a status payload indicating the
   *     application is running
   */
  @GetMapping
  @Nonnull
  public ResponseEntity<HealthResponse> health() {
    return ResponseEntity.ok(new HealthResponse("UP"));
  }
}
