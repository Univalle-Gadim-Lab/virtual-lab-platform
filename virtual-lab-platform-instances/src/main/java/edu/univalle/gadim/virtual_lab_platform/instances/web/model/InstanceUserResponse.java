package edu.univalle.gadim.virtual_lab_platform.instances.web.model;

import edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceUser;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Response DTO for instance-user association information.
 *
 * <p>This record contains the association between a user and a virtual lab instance.
 */
@ParametersAreNonnullByDefault
public record InstanceUserResponse(String id, String instanceId, String userId) {

  /**
   * Creates an InstanceUserResponse from an InstanceUser domain object.
   *
   * @param instanceUser the instance-user domain object
   * @return the response DTO
   */
  @Nonnull
  public static InstanceUserResponse from(InstanceUser instanceUser) {
    return new InstanceUserResponse(
        instanceUser.id(),
        instanceUser.instanceId(),
        instanceUser.userId());
  }
}
