package edu.univalle.gadim.virtual_lab_platform.commons.tool;

import static org.assertj.core.api.Assertions.assertThat;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

@NullMarked
@DisplayName("UuidGenerator")
class UuidGeneratorUnTest {

  private final UuidGenerator generator = new UuidGenerator();

  @Nested
  @DisplayName("generate()")
  class Generate {

    @Test
    @DisplayName("should return non-null string")
    void shouldReturnNonNullString() {
      final var result = generator.generate();

      assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("should return standard UUID format")
    void shouldReturnStandardUuidFormat() {
      final var result = generator.generate();

      assertThat(result)
          .hasSize(36)
          .matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
    }

    @RepeatedTest(10)
    @DisplayName("should generate unique ids on successive calls")
    void shouldGenerateUniqueIdsOnSuccessiveCalls() {
      final var first = generator.generate();
      final var second = generator.generate();

      assertThat(first).isNotEqualTo(second);
    }
  }

  @Nested
  @DisplayName("UniqueIdGenerator contract")
  class UniqueIdGeneratorContract {

    @Test
    @DisplayName("should implement UniqueIdGenerator")
    void shouldImplementUniqueIdGenerator() {
      assertThat(generator)
          .isInstanceOf(edu.univalle.gadim.virtual_lab_platform.commons.type.UniqueIdGenerator.class);
    }
  }
}
