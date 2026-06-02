package edu.univalle.gadim.virtual_lab_platform.instances.web.controller;

import edu.univalle.gadim.virtual_lab_platform.instances.web.model.CreateInstanceRequest;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.InstanceResponse;
import edu.univalle.gadim.virtual_lab_platform.instances.web.ops.InstancesWsOps;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for instance management operations.
 *
 * <p>This controller provides endpoints for creating, retrieving, starting, stopping, and deleting
 * virtual lab instances. All operations are delegated to {@link InstancesWsOps}, keeping this class
 * as a thin HTTP adapter that handles request routing and response status mapping.
 *
 * <h2>Endpoints</h2>
 * <ul>
 *   <li>{@code POST /api/instances} — create a new instance</li>
 *   <li>{@code GET /api/instances/{id}} — retrieve an instance by ID</li>
 *   <li>{@code GET /api/instances} — list instances for the current user</li>
 *   <li>{@code POST /api/instances/{id}/start} — start an instance</li>
 *   <li>{@code POST /api/instances/{id}/stop} — stop an instance</li>
 *   <li>{@code DELETE /api/instances/{id}} — delete an instance</li>
 * </ul>
 *
 * @see InstancesWsOps
 */
@RestController
@RequestMapping("/api/instances")
@ParametersAreNonnullByDefault
public class InstanceController {

  private static final Logger logger = LoggerFactory.getLogger(InstanceController.class);

  private final InstancesWsOps instancesWsOps;

  public InstanceController(InstancesWsOps instancesWsOps) {
    this.instancesWsOps = instancesWsOps;
  }

  /**
   * Creates a new instance.
   *
   * @param request the create instance request containing instance details
   * @return a {@code 200 OK} response with the created instance data
   */
  @PostMapping
  @Nonnull
  public ResponseEntity<InstanceResponse> createInstance(
      @RequestBody CreateInstanceRequest request) {
    logger.info("Creating instance with name: {}", request.name());
    return ResponseEntity.ok(instancesWsOps.createInstance(request));
  }

  /**
   * Retrieves an instance by ID.
   *
   * @param id the instance ID
   * @return a {@code 200 OK} response with the instance data, or {@code 404 Not Found}
   *     if no instance exists with the given ID
   */
  @GetMapping("/{id}")
  @Nonnull
  public ResponseEntity<InstanceResponse> getInstance(@PathVariable String id) {
    logger.debug("Retrieving instance by ID: {}", id);
    try {
      return ResponseEntity.ok(instancesWsOps.getInstanceById(id));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Retrieves all instances for the current user.
   *
   * @return a {@code 200 OK} response with the list of instance responses
   */
  @GetMapping
  @Nonnull
  public ResponseEntity<List<InstanceResponse>> getInstancesByUser() {
    logger.debug("Retrieving instances for current user");
    return ResponseEntity.ok(instancesWsOps.getInstancesByUser());
  }

  /**
   * Starts an instance.
   *
   * @param id the instance ID to start
   * @return a {@code 200 OK} response with the updated instance data, or {@code 404 Not Found}
   *     if no instance exists with the given ID
   */
  @PostMapping("/{id}/start")
  @Nonnull
  public ResponseEntity<InstanceResponse> startInstance(@PathVariable String id) {
    logger.info("Starting instance: {}", id);
    try {
      return ResponseEntity.ok(instancesWsOps.startInstance(id));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Stops an instance.
   *
   * @param id the instance ID to stop
   * @return a {@code 200 OK} response with the updated instance data, or {@code 404 Not Found}
   *     if no instance exists with the given ID
   */
  @PostMapping("/{id}/stop")
  @Nonnull
  public ResponseEntity<InstanceResponse> stopInstance(@PathVariable String id) {
    logger.info("Stopping instance: {}", id);
    try {
      return ResponseEntity.ok(instancesWsOps.stopInstance(id));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Deletes an instance.
   *
   * @param id the instance ID to delete
   * @return a {@code 204 No Content} response on success, or {@code 404 Not Found}
   *     if no instance exists with the given ID
   */
  @DeleteMapping("/{id}")
  @Nonnull
  public ResponseEntity<Void> deleteInstance(@PathVariable String id) {
    logger.info("Deleting instance: {}", id);
    try {
      instancesWsOps.deleteInstance(id);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    } catch (IllegalStateException e) {
      return ResponseEntity.status(409).build();
    }
  }
}
