package edu.univalle.gadim.virtual_lab_platform.instances.web.operation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import edu.univalle.gadim.virtual_lab_platform.instances.api.service.CatalogService;
import edu.univalle.gadim.virtual_lab_platform.instances.api.type.CatalogEntry;
import edu.univalle.gadim.virtual_lab_platform.instances.api.type.WorkspaceImage;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@NullMarked
@DisplayName("CatalogSpringWsOps")
@ExtendWith(MockitoExtension.class)
class CatalogSpringWsOpsUnTest {

  @Mock private CatalogService catalogService;

  private CatalogSpringWsOps wsOps;

  @BeforeEach
  void setUp() {
    wsOps = new CatalogSpringWsOps(catalogService);
  }

  private WorkspaceImage buildImage() {
    return new WorkspaceImage(
        "kicad",
        "KiCad",
        "Electronic design automation suite",
        "latest",
        "lab-kicad:latest",
        "EDA",
        4,
        8192,
        20480);
  }

  @Test
  @DisplayName("should map WorkspaceImage list to available images response")
  void shouldMapAvailableImages() {
    // Given
    when(catalogService.getAvailableImages()).thenReturn(List.of(buildImage()));

    // When
    final var response = wsOps.getAvailableImages();

    // Then
    assertThat(response)
        .hasSize(1)
        .first()
        .returns("kicad", edu.univalle.gadim.virtual_lab_platform.instances.web.model.WorkspaceImageResponse::id)
        .returns("KiCad", edu.univalle.gadim.virtual_lab_platform.instances.web.model.WorkspaceImageResponse::name)
        .returns("lab-kicad:latest", edu.univalle.gadim.virtual_lab_platform.instances.web.model.WorkspaceImageResponse::image);
  }

  @Test
  @DisplayName("should pair WorkspaceImage with running instance count in catalog entries")
  void shouldPairCatalogEntries() {
    // Given
    when(catalogService.getCatalog()).thenReturn(List.of(new CatalogEntry(buildImage(), 7L)));

    // When
    final var response = wsOps.getCatalog();

    // Then
    assertThat(response).hasSize(1);
    assertThat(response.get(0).image()).isNotNull();
    assertThat(response.get(0).runningInstanceCount()).isEqualTo(7L);
  }
}
