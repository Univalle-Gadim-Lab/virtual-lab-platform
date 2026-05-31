package edu.univalle.gadim.virtual_lab_platform.instances.operation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceStatus;
import edu.univalle.gadim.virtual_lab_platform.instances.api.type.WorkspaceImage;
import edu.univalle.gadim.virtual_lab_platform.instances.config.WorkspaceImageProperties;
import edu.univalle.gadim.virtual_lab_platform.instances.data.repository.InstanceRepository;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@NullMarked
@DisplayName("CatalogServiceOperation")
class CatalogServiceOperationUnTest {

  private static final WorkspaceImage KICAD_IMAGE =
      new WorkspaceImage(
          "kicad", "KiCad", "Electronic design automation suite",
          "latest", "lab-kicad:latest", "EDA", 2, 4096, 10240);

  private static final WorkspaceImage VIVADO_IMAGE =
      new WorkspaceImage(
          "vivado", "Vivado", "Xilinx FPGA design suite",
          "2023.2", "lab-vivado:2023.2", "FPGA", 4, 8192, 20480);

  @Mock private WorkspaceImageProperties workspaceImageProperties;

  @Mock private InstanceRepository instanceRepository;

  private CatalogServiceOperation catalogService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    catalogService = new CatalogServiceOperation(workspaceImageProperties, instanceRepository);
  }

  @Nested
  @DisplayName("getAvailableImages")
  class GetAvailableImages {

    @Test
    @DisplayName("should return configured images from properties")
    void shouldReturnConfiguredImages() {
      // Given
      when(workspaceImageProperties.getImages()).thenReturn(List.of(KICAD_IMAGE, VIVADO_IMAGE));

      // When
      final var result = catalogService.getAvailableImages();

      // Then
      assertThat(result).hasSize(2);
      assertThat(result.get(0).id()).isEqualTo("kicad");
      assertThat(result.get(1).id()).isEqualTo("vivado");
    }

    @Test
    @DisplayName("should return empty list when no images configured")
    void shouldReturnEmptyListWhenNoImagesConfigured() {
      // Given
      when(workspaceImageProperties.getImages()).thenReturn(List.of());

      // When
      final var result = catalogService.getAvailableImages();

      // Then
      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("getCatalog")
  class GetCatalog {

    @Test
    @DisplayName("should return catalog entries with running instance counts")
    void shouldReturnCatalogEntriesWithCounts() {
      // Given
      when(workspaceImageProperties.getImages()).thenReturn(List.of(KICAD_IMAGE, VIVADO_IMAGE));
      when(instanceRepository.countByImageNameAndStatusNot(
          "lab-kicad:latest", InstanceStatus.DELETED))
          .thenReturn(3L);
      when(instanceRepository.countByImageNameAndStatusNot(
          "lab-vivado:2023.2", InstanceStatus.DELETED))
          .thenReturn(0L);

      // When
      final var result = catalogService.getCatalog();

      // Then
      assertThat(result).hasSize(2);
      assertThat(result.get(0).image().id()).isEqualTo("kicad");
      assertThat(result.get(0).runningInstanceCount()).isEqualTo(3L);
      assertThat(result.get(1).image().id()).isEqualTo("vivado");
      assertThat(result.get(1).runningInstanceCount()).isEqualTo(0L);
    }

    @Test
    @DisplayName("should return empty catalog when no images configured")
    void shouldReturnEmptyCatalogWhenNoImagesConfigured() {
      // Given
      when(workspaceImageProperties.getImages()).thenReturn(List.of());

      // When
      final var result = catalogService.getCatalog();

      // Then
      assertThat(result).isEmpty();
    }
  }
}
