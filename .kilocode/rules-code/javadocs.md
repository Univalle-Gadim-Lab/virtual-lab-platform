# Java code documentation (Javadoc) format

- All Java classes must be documented with Javadoc.
- Documentation comments should be added ONLY to the following Java class sections:
    - Class definition
    - Constructors
    - Public and protected methods that do not override a method from a parent class or interface

## JavaDoc summary and description

Structure the JavaDocs contents using this format:

- Summary (required): Always ends with a period (.).
- Description:
    - Start every paragraph with '<p>' without a space after it.
    - Leave a blank line before every paragraph.

## Example

```java
/**
 * Represents a predicate node in a filter tree structure.
 *
 * <p>This class implements the FilterNode interface to provide leaf nodes that contain
 * actual filter conditions based on field comparisons.
 *
 * <p>Each predicate node consists of a field name, a comparison operator, and one or more
 * values to compare against.
 */
@EqualsAndHashCode
@ToString
@ParametersAreNonnullByDefault
public final class PredicateNode implements FilterNode {

  private static final List<FilterNode> children = List.of();

  private final String fieldName;

  private final ComparisonKey comparisonKey;

  private final List<Object> values;

  /**
   * Creates a new PredicateNode with the specified field, comparison, and values.
   *
   * <p>This constructor is annotated with @Builder to enable the builder pattern
   * for convenient node construction.
   *
   * @param fieldName     The name of the field to filter on.
   * @param comparisonKey The type of comparison to perform.
   * @param values        The values to compare against.
   */
  @Builder
  PredicateNode(String fieldName,
                ComparisonKey comparisonKey,
                @Singular List<Object> values) {
    this.fieldName = fieldName;
    this.comparisonKey = comparisonKey;
    this.values = List.copyOf(values);
  }

  /**
   * Returns the name of the field this predicate operates on.
   *
   * @return The field name for this predicate.
   */
  @Nonnull
  public String fieldName() {
    return fieldName;
  }

  /**
   * Returns the comparison operator for this predicate.
   *
   * @return The comparison key indicating how values should be compared.
   */
  @Nonnull
  public ComparisonKey comparisonKey() {
    return comparisonKey;
  }

  /**
   * Returns all comparison values for this predicate.
   *
   * @return An unmodifiable list of comparison values.
   */
  @Nonnull
  public List<Object> values() {
    return values;
  }

  /**
   * Returns a single comparison value when exactly one value is expected.
   *
   * <p>This method is used for predicates that require exactly one value, such as
   * equality comparisons.
   *
   * @return The single comparison value.
   * @throws IllegalStateException if the predicate contains zero or multiple values.
   */
  @Nonnull
  public Object value() {
    if (values.size() != 1) {
      throw new IllegalStateException("Expected a single value, but got: " + values);
    }
    return values.getFirst();
  }

  /**
   * Checks if this predicate has any comparison values.
   *
   * @return true if the predicate has at least one value, false otherwise.
   */
  public boolean hasValue() {
    return !values.isEmpty();
  }

  /**
   * Indicates whether this node can contain child nodes.
   *
   * <p>Always returns false for predicate nodes as they are meant to be leaf nodes
   * in the filter tree.
   *
   * @return false, indicating that this node type cannot contain children.
   */
  @Override
  public boolean allowsChildren() {
    return false;
  }

  /**
   * Returns the list of child nodes for this predicate.
   *
   * <p>Always returns an empty list as predicate nodes are leaf nodes and cannot
   * contain children.
   *
   * @return An empty, unmodifiable list.
   */
  @Override
  @Nonnull
  public List<FilterNode> children() {
    return children;
  }
}
```