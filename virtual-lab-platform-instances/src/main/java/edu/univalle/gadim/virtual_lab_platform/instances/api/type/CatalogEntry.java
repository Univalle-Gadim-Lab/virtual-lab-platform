package edu.univalle.gadim.virtual_lab_platform.instances.api.type;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A catalog entry combining a workspace image definition with its running instance count.
 *
 * @param image the workspace image definition
 * @param runningInstanceCount the number of non-deleted instances currently using this image
 */
@ParametersAreNonnullByDefault
public record CatalogEntry(WorkspaceImage image, long runningInstanceCount) {}
