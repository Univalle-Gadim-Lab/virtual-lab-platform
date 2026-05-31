package edu.univalle.gadim.virtual_lab_platform.instances.web.model;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Response DTO for a catalog entry combining a workspace image with its running instance count.
 *
 * @param image the workspace image definition
 * @param runningInstanceCount the number of non-deleted instances currently using this image
 */
@ParametersAreNonnullByDefault
public record CatalogEntryResponse(WorkspaceImageResponse image, long runningInstanceCount) {}
