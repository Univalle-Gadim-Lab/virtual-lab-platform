package edu.univalle.gadim.virtual_lab_platform.instances.web.controller;

import edu.univalle.gadim.virtual_lab_platform.instances.web.model.CatalogEntryResponse;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.WorkspaceImageResponse;
import edu.univalle.gadim.virtual_lab_platform.instances.web.ops.CatalogWsOps;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for workspace catalog operations.
 *
 * <p>Provides read-only endpoints for discovering available workspace images and
 * viewing the complete catalog with running instance counts. All authenticated
 * users can access these endpoints.
 *
 * <h2>Endpoints</h2>
 * <ul>
 *   <li>{@code GET /api/catalog/images} — list all available workspace images</li>
 *   <li>{@code GET /api/catalog} — full catalog with running instance counts</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/catalog")
@ParametersAreNonnullByDefault
public class CatalogController {

  private static final Logger logger = LoggerFactory.getLogger(CatalogController.class);

  private final CatalogWsOps catalogWsOps;

  public CatalogController(CatalogWsOps catalogWsOps) {
    this.catalogWsOps = catalogWsOps;
  }

  /**
   * Lists all available workspace images.
   *
   * @return a {@code 200 OK} response with the list of workspace images
   */
  @GetMapping("/images")
  @Nonnull
  public ResponseEntity<List<WorkspaceImageResponse>> getAvailableImages() {
    logger.debug("Retrieving available workspace images");
    return ResponseEntity.ok(catalogWsOps.getAvailableImages());
  }

  /**
   * Returns the complete workspace catalog with running instance counts.
   *
   * @return a {@code 200 OK} response with the list of catalog entries
   */
  @GetMapping
  @Nonnull
  public ResponseEntity<List<CatalogEntryResponse>> getCatalog() {
    logger.debug("Retrieving workspace catalog");
    return ResponseEntity.ok(catalogWsOps.getCatalog());
  }
}
