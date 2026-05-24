package edu.univalle.gadim.virtual_lab_platform.users.api.type;

/**
 * Defines system roles for authorization.
 *
 * <p>Roles are persisted as {@code VARCHAR} strings matching the constant names
 * and are assigned to users through the {@link UserRole} join entity.
 *
 * @see UserRole
 */
public enum Role {
    /**
     * Grants unrestricted administrative access to the platform.
     */
    ADMIN,
    /**
     * Standard student role with access to assigned virtual workspaces.
     */
    STUDENT,
    /**
     * Instructor role with privileges for managing student instances.
     */
    TEACHER
}
