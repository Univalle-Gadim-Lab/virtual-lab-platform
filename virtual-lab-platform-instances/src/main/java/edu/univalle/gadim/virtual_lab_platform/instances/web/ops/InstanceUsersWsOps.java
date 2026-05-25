package edu.univalle.gadim.virtual_lab_platform.instances.web.ops;

import edu.univalle.gadim.virtual_lab_platform.instances.web.model.CreateInstanceUserRequest;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.InstanceUserResponse;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * Web service operations contract for instance-user association management.
 *
 * <p>Defines one method per web endpoint exposed by the instance-users REST API.
 * Implementations bridge the HTTP layer to the underlying
 * {@link edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceUserService}
 * domain service, performing request-to-domain translation and domain-to-response mapping.
 *
 * <h2>Endpoints</h2>
 * <ul>
 *   <li>{@code POST /api/instance-users} — assign a user to an instance</li>
 *   <li>{@code GET /api/instance-users?instanceId=} — get users for an instance</li>
 *   <li>{@code GET /api/instance-users?userId=} — get instances for a user</li>
 *   <li>{@code DELETE /api/instance-users?userId=&instanceId=} — remove a user from an instance</li>
 * </ul>
 */
public interface InstanceUsersWsOps {

  /**
   * Assigns a user to an instance.
   *
   * @param request the create instance-user request containing user ID and instance ID
   * @return the created instance-user response
   */
  @Nonnull
  InstanceUserResponse assignUserToInstance(@Nonnull CreateInstanceUserRequest request);

  /**
   * Retrieves all users associated with a specific instance.
   *
   * @param instanceId the instance ID to retrieve users for
   * @return the list of instance-user responses, never null but may be empty
   */
  @Nonnull
  List<InstanceUserResponse> getUsersByInstanceId(@Nonnull String instanceId);

  /**
   * Retrieves all instances associated with a specific user.
   *
   * @param userId the user ID to retrieve instances for
   * @return the list of instance-user responses, never null but may be empty
   */
  @Nonnull
  List<InstanceUserResponse> getInstancesByUserId(@Nonnull String userId);

  /**
   * Removes a user from an instance.
   *
   * @param userId the user ID to remove
   * @param instanceId the instance ID to remove the user from
   */
  void removeUserFromInstance(@Nonnull String userId, @Nonnull String instanceId);
}
