package edu.univalle.gadim.virtual_lab_platform.instances.web.ops;

import edu.univalle.gadim.virtual_lab_platform.instances.web.model.CreateInstanceRequest;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.InstanceResponse;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * Web service operations contract for instance lifecycle management.
 *
 * <p>Defines one method per web endpoint exposed by the instances REST API.
 * Implementations bridge the HTTP layer to the underlying
 * {@link edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceService}
 * domain service, performing request-to-domain translation and domain-to-response mapping.
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
 */
public interface InstancesWsOps {

  /**
   * Creates a new virtual lab instance.
   *
   * @param request the create instance request containing instance details
   * @return the created instance response
   */
  @Nonnull
  InstanceResponse createInstance(@Nonnull CreateInstanceRequest request);

  /**
   * Retrieves an instance by its unique identifier.
   *
   * @param id the unique instance identifier
   * @return the instance response matching the given ID
   * @throws IllegalArgumentException if no instance is found with the given ID
   */
  @Nonnull
  InstanceResponse getInstanceById(@Nonnull String id);

  /**
   * Retrieves all instances belonging to the current user.
   *
   * @return the list of instance responses, never null but may be empty
   */
  @Nonnull
  List<InstanceResponse> getInstancesByUser();

  /**
   * Starts the specified instance.
   *
   * @param id the instance ID to start
   * @return the updated instance response
   * @throws IllegalArgumentException if no instance is found with the given ID
   */
  @Nonnull
  InstanceResponse startInstance(@Nonnull String id);

  /**
   * Stops the specified instance.
   *
   * @param id the instance ID to stop
   * @return the updated instance response
   * @throws IllegalArgumentException if no instance is found with the given ID
   */
  @Nonnull
  InstanceResponse stopInstance(@Nonnull String id);

  /**
   * Deletes the specified instance.
   *
   * @param id the instance ID to delete
   * @throws IllegalArgumentException if no instance is found with the given ID
   */
  void deleteInstance(@Nonnull String id);
}
