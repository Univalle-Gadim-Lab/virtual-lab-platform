package edu.univalle.gadim.virtual_lab_platform.instances.api.type;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Immutable value object representing a workspace image entry in the catalog.
 *
 * <p>Each workspace image defines the container image reference, a human-readable
 * name and description, a category for grouping, and default resource limits that
 * serve as the baseline configuration when creating instances from this image.
 *
 * @param id unique identifier for the workspace image
 * @param name human-readable display name
 * @param description brief description of the workspace purpose
 * @param version image version tag
 * @param image full Docker image reference (name:tag)
 * @param category grouping category (e.g. "EDA", "FPGA")
 * @param defaultCpuCores default CPU core allocation
 * @param defaultMemoryMb default memory allocation in megabytes
 * @param defaultStorageMb default storage allocation in megabytes
 */
@ParametersAreNonnullByDefault
public record WorkspaceImage(
    String id,
    String name,
    String description,
    String version,
    String image,
    String category,
    int defaultCpuCores,
    int defaultMemoryMb,
    int defaultStorageMb) {

  /**
   * Returns a new {@code WorkspaceImage} with the specified image reference.
   *
   * @param newImage the new Docker image reference
   * @return a new {@code WorkspaceImage} with the updated image reference
   */
  @Nonnull
  public WorkspaceImage withImage(String newImage) {
    return new WorkspaceImage(
        id, name, description, version, newImage, category,
        defaultCpuCores, defaultMemoryMb, defaultStorageMb);
  }
}
