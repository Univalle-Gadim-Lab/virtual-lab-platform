package edu.univalle.gadim.virtual_lab_platform.users.api.type;

/**
 * Defines the lifecycle states of a platform user.
 *
 * <p>Persisted as {@code VARCHAR} strings matching the constant names.
 *
 * @see User
 */
public enum UserStatus {
    /**
     * The user account is active and can authenticate to the platform.
     */
    ACTIVE,
    /**
     * The user account is disabled and cannot authenticate.
     */
    INACTIVE,
    /**
     * The user account has been permanently deactivated.
     *
     * <p>A user can only transition to this state from {@link #INACTIVE}.
     * Deleted users are retained for historical record integrity.
     */
    DELETED
}
