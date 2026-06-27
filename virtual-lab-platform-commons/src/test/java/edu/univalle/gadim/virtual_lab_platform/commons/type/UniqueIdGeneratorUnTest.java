package edu.univalle.gadim.virtual_lab_platform.commons.type;

import static org.assertj.core.api.Assertions.assertThat;

import edu.univalle.gadim.virtual_lab_platform.commons.tool.ObjectIdGenerator;
import edu.univalle.gadim.virtual_lab_platform.commons.tool.UuidGenerator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@NullMarked
@DisplayName("UniqueIdGenerator")
class UniqueIdGeneratorUnTest {

  @Nested
  @DisplayName("polymorphic generate() contract")
  class PolymorphicGenerateContract {

    @Test
    @DisplayName("ObjectIdGenerator should satisfy UniqueIdGenerator contract")
    void objectIdGeneratorShouldSatisfyContract() {
      final UniqueIdGenerator generator = new ObjectIdGenerator();
      final var result = generator.generate();

      assertThat(result).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("UuidGenerator should satisfy UniqueIdGenerator contract")
    void uuidGeneratorShouldSatisfyContract() {
      final UniqueIdGenerator generator = new UuidGenerator();
      final var result = generator.generate();

      assertThat(result).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("both implementations should produce distinct id formats")
    void bothImplementationsShouldProduceDistinctFormats() {
      final var objectId = new ObjectIdGenerator().generate();
      final var uuid = new UuidGenerator().generate();

      assertThat(objectId).hasSize(24).doesNotContain("-");
      assertThat(uuid).hasSize(36).contains("-");
    }
  }

  @Nested
  @DisplayName("uniqueness across implementations")
  class UniquenessAcrossImplementations {

    @Test
    @DisplayName("ids from both implementations should never collide in a batch")
    void idsFromBothImplementationsShouldNeverCollide() {
      final var objectIdGenerator = new ObjectIdGenerator();
      final var uuidGenerator = new UuidGenerator();
      final Set<String> allIds = new HashSet<>();

      final var objectIdIds = generateBatch(objectIdGenerator, 100);
      final var uuidIds = generateBatch(uuidGenerator, 100);

      allIds.addAll(objectIdIds);
      allIds.addAll(uuidIds);

      assertThat(allIds).hasSize(200);
    }

    private List<String> generateBatch(UniqueIdGenerator generator, int count) {
      return java.util.stream.Stream.generate(generator::generate)
          .limit(count)
          .toList();
    }
  }
}
