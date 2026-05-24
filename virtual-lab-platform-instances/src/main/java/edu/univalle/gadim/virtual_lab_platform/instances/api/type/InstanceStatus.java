package edu.univalle.gadim.virtual_lab_platform.instances.api.type;

/**
 * Defines the lifecycle states of a virtual lab instance.
 *
 * <p>Persisted as {@code VARCHAR} strings matching the constant names.
 * Transitions between states are managed by {@link edu.univalle.gadim.virtual_lab_platform.instances.operation.InstanceServiceOperation}.
 *
 * @see Instance
 */
public enum InstanceStatus {
    /**
     * The instance has been created and is being provisioned.
     */
    CREATED,
    /**
     * The instance's container is starting up.
     */
    STARTING,
    /**
     * The instance is running and accessible.
     */
    RUNNING,
    /**
     * The instance has been stopped and its container halted.
     */
    STOPPED,
    /**
     * The instance has exceeded its expiration time.
     */
    EXPIRED,
    /**
     * The instance has been permanently deleted.
     */
    DELETED
}