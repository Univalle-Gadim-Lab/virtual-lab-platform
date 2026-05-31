package edu.univalle.gadim.virtual_lab_platform.instances.config;

import edu.univalle.gadim.virtual_lab_platform.instances.api.type.WorkspaceImage;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties binding the workspace catalog image list from {@code application.yml}.
 *
 * <p>Bound to the {@code workspace.catalog} prefix. Each entry defines a workspace image
 * with its display metadata, Docker image reference, and default resource limits.
 *
 * @see WorkspaceImage
 */
@ConfigurationProperties(prefix = "workspace.catalog")
public class WorkspaceImageProperties {

  @Nonnull
  private List<WorkspaceImage> images = new ArrayList<>();

  @Nonnull
  public List<WorkspaceImage> getImages() {
    return images;
  }

  public void setImages(@Nonnull List<WorkspaceImage> images) {
    this.images = images;
  }
}
