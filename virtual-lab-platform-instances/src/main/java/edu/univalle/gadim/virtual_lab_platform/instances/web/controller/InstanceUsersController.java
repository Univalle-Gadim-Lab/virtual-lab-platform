package edu.univalle.gadim.virtual_lab_platform.instances.web.controller;

import edu.univalle.gadim.virtual_lab_platform.instances.web.model.CreateInstanceUserRequest;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.InstanceUserResponse;
import edu.univalle.gadim.virtual_lab_platform.instances.web.ops.InstanceUsersWsOps;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/instance-users")
@ParametersAreNonnullByDefault
public class InstanceUsersController {

  private static final Logger logger = LoggerFactory.getLogger(InstanceUsersController.class);

  private final InstanceUsersWsOps instanceUsersWsOps;

  public InstanceUsersController(InstanceUsersWsOps instanceUsersWsOps) {
    this.instanceUsersWsOps = instanceUsersWsOps;
  }

  @PostMapping
  @Nonnull
  public ResponseEntity<InstanceUserResponse> assignUserToInstance(
      @RequestBody CreateInstanceUserRequest request) {
    logger.info("Assigning user {} to instance {}", request.userId(), request.instanceId());
    return ResponseEntity.ok(instanceUsersWsOps.assignUserToInstance(request));
  }

  @GetMapping
  @Nonnull
  public ResponseEntity<List<InstanceUserResponse>> getInstanceUserAssociations(
      @RequestParam(required = false) @Nullable String instanceId,
      @RequestParam(required = false) @Nullable String userId) {
    if (instanceId != null && userId == null) {
      logger.debug("Getting users for instance: {}", instanceId);
      return ResponseEntity.ok(instanceUsersWsOps.getUsersByInstanceId(instanceId));
    }
    if (userId != null && instanceId == null) {
      logger.debug("Getting instances for user: {}", userId);
      return ResponseEntity.ok(instanceUsersWsOps.getInstancesByUserId(userId));
    }
    logger.warn("Invalid query parameters: exactly one of instanceId or userId must be provided");
    return ResponseEntity.badRequest().build();
  }

  @DeleteMapping
  @Nonnull
  public ResponseEntity<Void> removeUserFromInstance(
      @RequestParam String userId, @RequestParam String instanceId) {
    logger.info("Removing user {} from instance {}", userId, instanceId);
    instanceUsersWsOps.removeUserFromInstance(userId, instanceId);
    return ResponseEntity.noContent().build();
  }
}
