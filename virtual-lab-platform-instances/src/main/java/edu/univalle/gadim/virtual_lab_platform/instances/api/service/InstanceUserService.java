package edu.univalle.gadim.virtual_lab_platform.instances.api.service;

import edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceUser;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * Service interface for managing instance-user associations.
 *
 * <p>This interface defines operations for managing the relationships between
 * users and instances, including ownership and access control.
 */
public interface InstanceUserService {

    /**
     * Assigns a user to an instance.
     *
     * @param userId The user ID
     * @param instanceId The instance ID
     * @return The created instance-user association
     */
    @Nonnull
    InstanceUser assignUserToInstance(@Nonnull String userId, @Nonnull String instanceId);

    /**
     * Retrieves all users associated with a specific instance.
     *
     * @param instanceId The instance ID
     * @return A list of instance-user associations
     */
    @Nonnull
    List<InstanceUser> getUsersByInstanceId(@Nonnull String instanceId);

    /**
     * Retrieves all instances associated with a specific user.
     *
     * @param userId The user ID
     * @return A list of instance-user associations
     */
    @Nonnull
    List<InstanceUser> getInstancesByUserId(@Nonnull String userId);

    /**
     * Removes a user from an instance.
     *
     * @param userId The user ID
     * @param instanceId The instance ID
     */
    void removeUserFromInstance(@Nonnull String userId, @Nonnull String instanceId);
}