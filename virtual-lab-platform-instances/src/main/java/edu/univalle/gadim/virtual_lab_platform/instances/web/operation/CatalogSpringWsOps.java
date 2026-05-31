package edu.univalle.gadim.virtual_lab_platform.instances.web.operation;

import edu.univalle.gadim.virtual_lab_platform.instances.api.service.CatalogService;
import edu.univalle.gadim.virtual_lab_platform.instances.api.type.WorkspaceImage;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.CatalogEntryResponse;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.WorkspaceImageResponse;
import edu.univalle.gadim.virtual_lab_platform.instances.web.ops.CatalogWsOps;
import java.util.List;
import javax.annotation.Nonnull;
import org.springframework.stereotype.Component;

/**
 * Concrete implementation of {@link CatalogWsOps} that delegates to the
 * {@link CatalogService} domain service.
 *
 * <p>This class acts as the bridge between the HTTP contract layer and the
 * business logic layer. It translates domain objects into response DTOs
 * suitable for HTTP serialization.
 */
@Component
public class CatalogSpringWsOps implements CatalogWsOps {

  private final CatalogService catalogService;

  public CatalogSpringWsOps(CatalogService catalogService) {
    this.catalogService = catalogService;
  }

  @Override
  @Nonnull
  public List<WorkspaceImageResponse> getAvailableImages() {
    return catalogService.getAvailableImages().stream()
        .map(CatalogSpringWsOps::toResponse)
        .toList();
  }

  @Override
  @Nonnull
  public List<CatalogEntryResponse> getCatalog() {
    return catalogService.getCatalog().stream()
        .map(
            entry ->
                new CatalogEntryResponse(
                    toResponse(entry.image()), entry.runningInstanceCount()))
        .toList();
  }

  @Nonnull
  private static WorkspaceImageResponse toResponse(WorkspaceImage image) {
    return new WorkspaceImageResponse(
        image.id(),
        image.name(),
        image.description(),
        image.version(),
        image.image(),
        image.category());
  }
}
