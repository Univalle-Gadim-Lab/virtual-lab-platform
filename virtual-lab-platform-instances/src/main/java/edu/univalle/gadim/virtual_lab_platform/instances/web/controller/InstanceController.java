package edu.univalle.gadim.virtual_lab_platform.instances.web.controller;

import edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceService;
import edu.univalle.gadim.virtual_lab_platform.instances.api.type.Instance;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.CreateInstanceRequest;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.InstanceResponse;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for instance management operations.
 *
 * <p>This controller provides endpoints for creating, retrieving, starting, stopping, and deleting
 * virtual lab instances.
 */
@RestController
@RequestMapping("/api/instances")
@ParametersAreNonnullByDefault
public class InstanceController {

  private static final Logger logger = LoggerFactory.getLogger(InstanceController.class);

  private final InstanceService instanceService;

  public InstanceController(InstanceService instanceService) {
    this.instanceService = instanceService;
  }

  /**
   * Creates a new instance.
   *
   * @param request the create instance request containing instance details
   * @return the created instance response
   */
  @PostMapping
  @Nonnull
  public ResponseEntity<InstanceResponse> createInstance(
      @RequestBody CreateInstanceRequest request) {
    logger.info("Creating instance with name: {}", request.name());

    // Get user ID from security context (placeholder - should be extracted from JWT)
    String userId = "current-user-id";

    Instance instance =
        instanceService.createInstance(
            userId,
            request.name(),
            Optional.ofNullable(request.description()),
            request.imageName(),
            request.imageVersion(),
            request.imageRegistry(),
            request.cpuCores(),
            request.memoryMb(),
            request.storageMb(),
            request.gpuEnabled(),
            request.exposedPort());

    return ResponseEntity.ok(InstanceResponse.from(instance));
  }

  /**
   * Retrieves an instance by ID.
   *
   * @param id the instance ID
   * @return the instance response or 404 if not found
   */
  @GetMapping("/{id}")
  @Nonnull
  public ResponseEntity<InstanceResponse> getInstance(@PathVariable String id) {
    logger.debug("Retrieving instance by ID: {}", id);

    return instanceService
        .getInstanceById(id)
        .map(instance -> ResponseEntity.ok(InstanceResponse.from(instance)))
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * Retrieves all instances for the current user.
   *
   * @return the list of instance responses
   */
  @GetMapping
  @Nonnull
  public ResponseEntity<List<InstanceResponse>> getInstancesByUser() {
    logger.debug("Retrieving instances for current user");

    // Get user ID from security context (placeholder - should be extracted from JWT)
    String userId = "current-user-id";

    List<InstanceResponse> responses =
        instanceService.getInstancesByUserId(userId).stream().map(InstanceResponse::from).toList();

    return ResponseEntity.ok(responses);
  }

  /**
   * Starts an instance.
   *
   * @param id the instance ID to start
   * @return the updated instance response
   */
  @PostMapping("/{id}/start")
  @Nonnull
  public ResponseEntity<InstanceResponse> startInstance(@PathVariable String id) {
    logger.info("Starting instance: {}", id);

    Instance instance = instanceService.startInstance(id);
    return ResponseEntity.ok(InstanceResponse.from(instance));
  }

  /**
   * Stops an instance.
   *
   * @param id the instance ID to stop
   * @return the updated instance response
   */
  @PostMapping("/{id}/stop")
  @Nonnull
  public ResponseEntity<InstanceResponse> stopInstance(@PathVariable String id) {
    logger.info("Stopping instance: {}", id);

    Instance instance = instanceService.stopInstance(id);
    return ResponseEntity.ok(InstanceResponse.from(instance));
  }

  /**
   * Deletes an instance.
   *
   * @param id the instance ID to delete
   * @return 204 No Content on success
   */
  @DeleteMapping("/{id}")
  @Nonnull
  public ResponseEntity<Void> deleteInstance(@PathVariable String id) {
    logger.info("Deleting instance: {}", id);

    instanceService.deleteInstance(id);
    return ResponseEntity.noContent().build();
  }
}
