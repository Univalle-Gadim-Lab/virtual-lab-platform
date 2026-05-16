package edu.univalle.gadim.virtual_lab_platform.instances.api.type;

import javax.annotation.Nonnull;

/**
 * Represents the association between an instance and a user in the virtual lab platform.
 *
 * <p>This interface defines the relationship that establishes ownership and access
 * rights for users to specific instances.
 */
public interface InstanceUser {

    /**
     * Returns the unique identifier of the instance-user association.
     *
     * @return The association ID
     */
    @Nonnull
    String id();

    /**
     * Returns the ID of the instance.
     *
     * @return The instance ID
     */
    @Nonnull
    String instanceId();

    /**
     * Returns the ID of the user associated with the instance.
     *
     * @return The user ID
     */
    @Nonnull
    String userId();
}