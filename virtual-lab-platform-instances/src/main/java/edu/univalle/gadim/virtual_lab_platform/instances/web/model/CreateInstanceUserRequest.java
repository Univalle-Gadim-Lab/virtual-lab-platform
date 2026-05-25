package edu.univalle.gadim.virtual_lab_platform.instances.web.model;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Request DTO for assigning a user to an instance.
 *
 * <p>This record contains the user ID and instance ID to create an association
 * between a user and a virtual lab instance.
 */
@ParametersAreNonnullByDefault
public record CreateInstanceUserRequest(String userId, String instanceId) {}
