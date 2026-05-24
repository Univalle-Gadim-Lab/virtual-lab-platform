---
name: javadoc
description: Standards for writing and maintaining JavaDoc documentation in Java projects. Use when writing, updating, or reviewing JavaDoc for classes, methods, packages, fields, enums, or annotations. Handles conditional loading of detailed standards from reference files based on documentation context.
---

# JavaDoc Documentation Skill

Standards for writing high-quality JavaDoc documentation with consistency, completeness, and maintainability.

## Workflow

### Step 1: Load Applicable Standards

**CRITICAL**: Always load foundational standards first, then conditionally load context-specific references.

1. **Load core standards**:
   ```
   Read: references/javadoc-core.md
   ```
   Mandatory documentation requirements, core principles, basic tag usage, tag order, anti-patterns.

2. **Conditional loading based on context**:

   | If documenting... | Load this reference |
   |------------------|---------------------|
   | Classes, interfaces, packages, enums, annotations | `references/javadoc-class-documentation.md` |
   | Methods, fields, constructors | `references/javadoc-method-documentation.md` |
   | Code examples or complex formatting | `references/javadoc-code-examples.md` |

3. **Extract key requirements** and store in working memory for task execution.

### Step 2: Analyze Existing Documentation

When updating existing JavaDoc:

1. **Identify gaps**: Check public/protected APIs lack documentation, incomplete param/return/exception docs, outdated content.
2. **Assess quality**: Review clarity, verify {@link} references valid, check tag order.
3. **Check consistency**: Terminology, style, similar APIs documented similarly.

### Step 3: Write/Update JavaDoc

1. **Apply core principles**:
   - Start with clear purpose statement (what and why)
   - Avoid stating the obvious
   - Focus on behavior/contracts, not implementation
   - Keep documentation synchronized with code

2. **Use proper tag structure**:
   - `@param` - all parameters with validation rules
   - `@return` - return value guarantees and null handling
   - `@throws` - all exceptions with conditions
   - `@see` - cross-references
   - `@since` - version for public APIs
   - `@deprecated` - migration path with replacement

3. **Follow standard tag order**:
   ```
   @param → @return → @throws → @see → @since → @deprecated
   ```

### Step 4: Verify and Report

1. **Verify standards compliance**:
   - All public/protected APIs documented
   - No "stating the obvious" documentation
   - Proper tag order followed
   - All {@link} references valid

2. **Report results**:
   - Documentation created/updated
   - Standards applied
   - Any deviations and justifications

## Gotchas

- **Package-info.java**: Every package **must** have a package-info.java file with documentation.
- **"Stating the obvious"**: `{@code userId}` is redundant if param is named `userId`. Document the **purpose** and **constraints** instead.
- **{@link} vs @see**: Use `{@link ClassName#method}` for inline references; use `@see` for end-of-block cross-references.
- **HTML in code examples**: Inside `<pre><code>` blocks, escape HTML entities only if needed for display (e.g., `&lt;` for `<` inside generic types).
- **Generic type parameters**: Document `<T>` parameters with `@param <T>` at the class level, not in individual methods.
- **serialVersionUID and LOGGER**: Do **not** document these private fields—they are obvious and boilerplate.

## Common Tasks

### Task: Document a new public class

1. Load javadoc-core.md + javadoc-class-documentation.md
2. Add class-level JavaDoc with purpose, thread-safety, example
3. Document all public constructors
4. Document all public methods with params/returns/exceptions
5. Add @since tag with current version

### Task: Add code examples to existing documentation

1. Load javadoc-core.md + javadoc-code-examples.md
2. Identify methods needing examples (complex APIs, common use cases)
3. Write complete, compilable examples with error handling
4. Use proper `<pre><code>` formatting

### Task: Update documentation for API changes

1. Load javadoc-core.md + javadoc-method-documentation.md
2. Review changed methods/classes
3. Update parameter/return/exception documentation
4. Add @deprecated tags if removing APIs
5. Update @since tags if adding new parameters
6. Verify all {@link} references still valid

## References

| Reference | Purpose |
|-----------|---------|
| `references/javadoc-core.md` | Core principles, tag usage, anti-patterns, tag order |
| `references/javadoc-class-documentation.md` | Package-info, classes, interfaces, enums, annotations |
| `references/javadoc-method-documentation.md` | Methods, fields, constructors, builder patterns |
| `references/javadoc-code-examples.md` | {@code}, {@link}, code blocks, HTML formatting |
