package edu.univalle.gadim.virtual_lab_platform.boot.config;

import static org.assertj.core.api.Assertions.assertThat;

import edu.univalle.gadim.virtual_lab_platform.commons.tool.ObjectIdGenerator;
import edu.univalle.gadim.virtual_lab_platform.commons.type.UniqueIdGenerator;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@NullMarked
@DisplayName("BootConfig")
class BootConfigUnTest {

  @Test
  @DisplayName("should provide a UniqueIdGenerator bound to ObjectIdGenerator")
  void shouldProvideObjectIdGeneratorImplementation() {
    // Given
    final var config = new BootConfig();

    // When
    final var generator = config.uniqueIdGenerator();

    // Then
    assertThat(generator).isNotNull().isInstanceOf(ObjectIdGenerator.class);
  }

  @Test
  @DisplayName("should generate an ObjectId-format identifier from the wired bean")
  void shouldGenerateValidObjectId() {
    // Given
    final var config = new BootConfig();

    // When
    final var generator = config.uniqueIdGenerator();
    final var id = generator.generate();

    // Then
    assertThat(id)
        .isNotNull()
        .hasSize(24)
        .matches("^[a-f0-9]{24}$");
  }
}
