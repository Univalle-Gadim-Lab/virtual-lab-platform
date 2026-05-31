package edu.univalle.gadim.virtual_lab_platform.instances.web.ops;

import edu.univalle.gadim.virtual_lab_platform.instances.web.model.CatalogEntryResponse;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.WorkspaceImageResponse;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * Web service operations contract for workspace catalog endpoints.
 *
 * <p>Defines methods for catalog image discovery and catalog listing
 * enriched with running instance counts.
 */
public interface CatalogWsOps {

  /**
   * Returns the list of all available workspace images.
   *
   * @return the list of workspace image responses, never null but may be empty
   */
  @Nonnull
  List<WorkspaceImageResponse> getAvailableImages();

  /**
   * Returns the complete catalog with each workspace image enriched with its running instance count.
   *
   * @return the list of catalog entry responses, never null but may be empty
   */
  @Nonnull
  List<CatalogEntryResponse> getCatalog();
}
