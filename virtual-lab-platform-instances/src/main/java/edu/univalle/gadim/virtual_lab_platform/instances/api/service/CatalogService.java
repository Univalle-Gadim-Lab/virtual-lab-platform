package edu.univalle.gadim.virtual_lab_platform.instances.api.service;

import edu.univalle.gadim.virtual_lab_platform.instances.api.type.CatalogEntry;
import edu.univalle.gadim.virtual_lab_platform.instances.api.type.WorkspaceImage;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * Service interface for workspace catalog operations.
 *
 * <p>Provides read-only access to the available workspace images and the full catalog
 * enriched with running instance counts.
 */
public interface CatalogService {

  /**
   * Returns the list of all configured workspace images.
   *
   * @return an immutable list of workspace images, never null but may be empty
   */
  @Nonnull
  List<WorkspaceImage> getAvailableImages();

  /**
   * Returns the complete catalog with each workspace image enriched with its running instance count.
   *
   * @return an immutable list of catalog entries, never null but may be empty
   */
  @Nonnull
  List<CatalogEntry> getCatalog();
}
