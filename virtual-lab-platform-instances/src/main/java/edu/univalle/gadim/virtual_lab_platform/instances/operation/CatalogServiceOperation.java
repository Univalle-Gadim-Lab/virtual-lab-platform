package edu.univalle.gadim.virtual_lab_platform.instances.operation;

import edu.univalle.gadim.virtual_lab_platform.instances.api.service.CatalogService;
import edu.univalle.gadim.virtual_lab_platform.instances.api.type.CatalogEntry;
import edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceStatus;
import edu.univalle.gadim.virtual_lab_platform.instances.api.type.WorkspaceImage;
import edu.univalle.gadim.virtual_lab_platform.instances.config.WorkspaceImageProperties;
import edu.univalle.gadim.virtual_lab_platform.instances.data.repository.InstanceRepository;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for workspace catalog operations.
 *
 * <p>Reads workspace image definitions from YAML configuration and enriches them
 * with live instance counts queried from the repository.
 */
@Service
@Transactional(readOnly = true)
@ParametersAreNonnullByDefault
public class CatalogServiceOperation implements CatalogService {

  private static final Logger logger = LoggerFactory.getLogger(CatalogServiceOperation.class);

  private final WorkspaceImageProperties workspaceImageProperties;
  private final InstanceRepository instanceRepository;

  public CatalogServiceOperation(
      WorkspaceImageProperties workspaceImageProperties,
      InstanceRepository instanceRepository) {
    this.workspaceImageProperties = workspaceImageProperties;
    this.instanceRepository = instanceRepository;
  }

  @Override
  @Nonnull
  public List<WorkspaceImage> getAvailableImages() {
    logger.debug("Retrieving available workspace images");
    return List.copyOf(workspaceImageProperties.getImages());
  }

  @Override
  @Nonnull
  public List<CatalogEntry> getCatalog() {
    logger.debug("Retrieving workspace catalog with instance counts");
    return workspaceImageProperties.getImages().stream()
        .map(this::toCatalogEntry)
        .toList();
  }

  @Nonnull
  private CatalogEntry toCatalogEntry(WorkspaceImage image) {
    final var count =
        instanceRepository.countByImageNameAndStatusNot(
            image.image(), InstanceStatus.DELETED);
    return new CatalogEntry(image, count);
  }
}
