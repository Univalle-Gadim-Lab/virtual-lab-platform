package edu.univalle.gadim.virtual_lab_platform.instances.web.model;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Response DTO for a workspace image entry in the catalog.
 *
 * @param id unique identifier for the workspace image
 * @param name human-readable display name
 * @param description brief description of the workspace purpose
 * @param version image version tag
 * @param image full Docker image reference (name:tag)
 * @param category grouping category
 */
@ParametersAreNonnullByDefault
public record WorkspaceImageResponse(
    String id,
    String name,
    String description,
    String version,
    String image,
    String category) {}
