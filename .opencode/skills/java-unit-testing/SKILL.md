---
name: java-unit-testing
description: Use when you need to review, improve, or write Java unit tests. Covers JUnit 5, AssertJ assertions, Mockito mocking, Given-When-Then structure, parameterized tests, and test naming conventions.
---

# Java Unit Testing Guidelines

Guidelines for writing clear, maintainable, and effective Java unit tests.

> **Prerequisites**: This skill requires the `java-core` and `java-null-safety` skills. All test code must follow Google Java Style and use JSpecify nullness annotations where applicable.

## Test Class Naming

Test classes must be named using the convention `<ClassName>UnTest`:

```java
// Correct
class PaymentServiceUnTest { ... }
class UserRepositoryUnTest { ... }

// Incorrect
class PaymentServiceTest { ... }
class PaymentServiceTests { ... }
class TestPaymentService { ... }
```

## Test Scope and Visibility

Test classes and methods must have package-private visibility:

```java
// Correct
@NullMarked
class PaymentServiceUnTest {
  @Test
  void shouldReturnPaymentWhenIdExists() { ... }
}

// Incorrect
public class PaymentServiceUnTest {
  @Test
  public void shouldReturnPaymentWhenIdExists() { ... }
}
```

## Core Principles

### 1. Clarity and Readability

Tests should be easy to understand through descriptive names, clear Given-When-Then structure, and focused assertions. Readable tests serve as living documentation.

### 2. Isolation and Independence

Each test must be self-contained, not relying on the state or outcome of other tests. Dependencies should be mocked to ensure the unit under test is validated in isolation.

### 3. Comprehensive Validation

Tests should thoroughly verify behavior including valid inputs, edge cases, boundary conditions, and error scenarios. Cover both positive paths and how code handles failures.

### 4. Modern Tooling and Practices

Leverage JUnit 5, AssertJ fluent assertions, and Mockito. Utilize parameterized tests to reduce boilerplate and improve coverage of data variations.

### 5. Maintainability and Focus

Tests should be easy to maintain. Avoid complex tests that verify implementation details or have multiple responsibilities.

## JUnit 5 Annotations

Use annotations from `org.junit.jupiter.api`:

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
```

## AssertJ for Assertions

Use AssertJ's fluent API for readable, expressive assertions:

```java
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Correct
assertThat(result).isNotNull();
assertThat(result.getStatus()).isEqualTo(Status.ACTIVE);
assertThat(items).hasSize(3).containsExactlyInAnyOrder("a", "b", "c");

// Incorrect (JUnit assertions)
assertEquals(Status.ACTIVE, result.getStatus());
assertTrue(result != null);
```

### Chaining Assertions on the Same Object

Chain assertions targeting the same object to avoid duplication and increase clarity:

```java
// Correct - chained assertions
assertThat(user)
  .isNotNull()
  .extracting("name", "email", "status")
  .containsExactly("John", "john@example.com", Status.ACTIVE);

assertThat(response)
  .isNotNull()
  .extracting(Response::getStatusCode, Response::getBody)
  .contains(200, "OK");

// Incorrect - multiple assertThat on same object
assertThat(user).isNotNull();
assertThat(user.getName()).isEqualTo("John");
assertThat(user.getEmail()).isEqualTo("john@example.com");
assertThat(user.getStatus()).isEqualTo(Status.ACTIVE);
```

## Given-When-Then Structure

Organize test logic into three distinct phases with visual separation:

```java
@Test
@DisplayName("should return payment when id exists")
void shouldReturnPaymentWhenIdExists() {
  // Given
  final var paymentId = "PAY-001";
  final var expectedPayment = new Payment(paymentId, BigDecimal.TEN);
  when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(expectedPayment));

  // When
  final var result = paymentService.findById(paymentId);

  // Then
  assertThat(result).isPresent();
  assertThat(result.get().paymentId()).isEqualTo(paymentId);
}
```

Use comments or empty lines to visually separate phases.

## Descriptive Test Names

Test names should communicate the scenario and expected outcome. Use `@DisplayName` for natural language descriptions:

```java
// Correct
@Test
@DisplayName("should throw PaymentNotFoundException when payment does not exist")
void shouldThrowExceptionWhenPaymentNotFound() { ... }

@Test
@DisplayName("should calculate total with discount applied")
void shouldCalculateTotalWithDiscount() { ... }

// Incorrect
@Test
void testPaymentService() { ... }

@Test
void test1() { ... }
```

## Single Responsibility in Tests

Each test should focus on one specific aspect of behavior:

```java
// Correct - separate tests for different behaviors
@Test
@DisplayName("should return empty when user not found")
void shouldReturnEmptyWhenUserNotFound() { ... }

@Test
@DisplayName("should return user when found")
void shouldReturnUserWhenFound() { ... }

// Incorrect - testing multiple things
@Test
void testUserRepository() {
  // tests both found and not found cases
  // tests validation logic
  // tests exception handling
}
```

## Test Independence

Tests must not depend on state from other tests:

```java
@NullMarked
class UserServiceUnTest {
  private UserRepository userRepository;
  private UserService userService;

  @BeforeEach
  void setUp() {
    userRepository = mock(UserRepository.class);
    userService = new UserService(userRepository);
  }

  @Test
  void testOne() {
    // Uses fresh userService instance
  }

  @Test
  void testTwo() {
    // Uses fresh userService instance - no dependency on testOne
  }
}
```

## Parameterized Tests for Data Variations

Use `@ParameterizedTest` to test the same logic with different inputs:

```java
@ParameterizedTest
@DisplayName("should reject invalid email formats")
@ValueSource(strings = {"invalid", "@no-local", "no-domain@", "spaces in@email.com"})
void shouldRejectInvalidEmails(String invalidEmail) {
  assertThatThrownBy(() -> userService.validateEmail(invalidEmail))
    .isInstanceOf(InvalidEmailException.class);
}

@ParameterizedTest
@DisplayName("should calculate discount correctly")
@CsvSource({
  "100.00, 0.10, 90.00",
  "200.00, 0.20, 160.00",
  "50.00, 0.00, 50.00"
})
void shouldCalculateDiscountCorrectly(BigDecimal amount, BigDecimal rate, BigDecimal expected) {
  final var result = discountCalculator.apply(amount, rate);
  assertThat(result).isEqualByComparingTo(expected);
}

@ParameterizedTest
@DisplayName("should validate valid user names")
@MethodSource("validNamesProvider")
void shouldAcceptValidNames(String name) {
  assertThat(userService.isValidName(name)).isTrue();
}

static Stream<String> validNamesProvider() {
  return Stream.of("John", "Jane Doe", "O'Brien", "Mary-Jane");
}
```

## Mocking with Mockito

Use Mockito to isolate the System Under Test (SUT):

```java
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@NullMarked
class PaymentServiceUnTest {
  private PaymentRepository paymentRepository;
  private NotificationService notificationService;
  private PaymentService paymentService;

  @BeforeEach
  void setUp() {
    paymentRepository = mock(PaymentRepository.class);
    notificationService = mock(NotificationService.class);
    paymentService = new PaymentService(paymentRepository, notificationService);
  }

  @Test
  @DisplayName("should save payment and send notification")
  void shouldSavePaymentAndNotify() {
    // Given
    final var payment = new Payment("PAY-001", BigDecimal.TEN);
    when(paymentRepository.save(any())).thenReturn(payment);

    // When
    paymentService.processPayment(payment);

    // Then
    verify(paymentRepository).save(payment);
    verify(notificationService).sendPaymentConfirmation(payment);
  }

  @Test
  @DisplayName("should not notify when save fails")
  void shouldNotNotifyOnSaveFailure() {
    // Given
    final var payment = new Payment("PAY-001", BigDecimal.TEN);
    when(paymentRepository.save(any())).thenThrow(new DatabaseException("Connection failed"));

    // When / Then
    assertThatThrownBy(() -> paymentService.processPayment(payment))
      .isInstanceOf(DatabaseException.class);

    verify(notificationService, never()).sendPaymentConfirmation(any());
  }
}
```

### Mockito Annotation Alternative

```java
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
@NullMarked
class PaymentServiceUnTest {
  @Mock
  private PaymentRepository paymentRepository;

  @Mock
  private NotificationService notificationService;

  @InjectMocks
  private PaymentService paymentService;

  // tests...
}
```

## Nested Test Classes

Use `@Nested` to group related tests:

```java
@NullMarked
class UserServiceUnTest {
  private UserRepository repository;
  private UserService service;

  @BeforeEach
  void setUp() {
    repository = mock(UserRepository.class);
    service = new UserService(repository);
  }

  @Nested
  @DisplayName("user creation")
  class UserCreation {
    @Test
    @DisplayName("should create user with valid data")
    void shouldCreateUserWithValidData() { ... }

    @Test
    @DisplayName("should reject duplicate email")
    void shouldRejectDuplicateEmail() { ... }
  }

  @Nested
  @DisplayName("user lookup")
  class UserLookup {
    @Test
    @DisplayName("should find user by id")
    void shouldFindUserById() { ... }

    @Test
    @DisplayName("should return empty when not found")
    void shouldReturnEmptyWhenNotFound() { ... }
  }
}
```

## Code Splitting Strategies

### Small Test Methods

Keep test methods small and focused:

```java
// Correct - small, focused
@Test
@DisplayName("should return user name in uppercase")
void shouldReturnUserNameInUppercase() {
  final var user = new User("john");
  assertThat(user.getNameUppercase()).isEqualTo("JOHN");
}

// Incorrect - doing too much
@Test
void testUser() {
  var user = new User("john");
  assertEquals("JOHN", user.getNameUppercase());
  assertEquals("john", user.getName());
  assertTrue(user.isActive());
  user.setActive(false);
  assertFalse(user.isActive());
}
```

### Helper Methods

Use helper methods to avoid duplication:

```java
@NullMarked
class PaymentServiceUnTest {
  // ...

  private Payment createTestPayment(String id) {
    return new Payment(id, BigDecimal.TEN);
  }

  private Payment createTestPayment() {
    return createTestPayment("PAY-DEFAULT");
  }

  @Test
  @DisplayName("should process payment")
  void shouldProcessPayment() {
    final var payment = createTestPayment("PAY-001");
    // test logic
  }
}
```

## Anti-patterns and Code Smells

- **Testing Implementation Details:** Avoid testing internal details that might change. Focus on behavior and outcomes.
- **Hard-coded Values:** Avoid magic numbers and strings. Use constants or test data factories.
- **Complex Test Logic:** Keep test logic simple. Avoid complex calculations or conditionals within tests.
- **Ignoring Edge Cases:** Don't ignore edge cases or boundary conditions. Test invalid and unexpected values.
- **Slow Tests:** Avoid slow tests that discourage frequent execution.
- **Over-reliance on Mocks:** Mock judiciously. Too many mocks obscure actual behavior and reduce reliability.
- **Chained Mocks:** Avoid deeply nested mock chains (`mockA.getB().getC().getD()`). Extract collaborators.
- **Test Interdependence:** Never create tests that depend on other tests' outcomes or state.

## State Management

- **Isolated State:** Each test has its own isolated state. Use `@BeforeEach` to reset state.
- **Immutable Objects:** Prefer immutable objects to simplify state management and avoid side effects.
- **Stateless Components:** Design stateless components to reduce state management complexity.

```java
@NullMarked
class CalculatorUnTest {
  private Calculator calculator;

  @BeforeEach
  void setUp() {
    calculator = new Calculator();  // Fresh state for each test
  }

  @Test
  @DisplayName("should add two numbers")
  void shouldAddNumbers() {
    assertThat(calculator.add(2, 3)).isEqualTo(5);
  }

  @Test
  @DisplayName("should subtract two numbers")
  void shouldSubtractNumbers() {
    assertThat(calculator.subtract(5, 3)).isEqualTo(2);
  }
}
```

## Error Handling

### Expected Exceptions

Use `assertThatThrownBy` to verify exceptions:

```java
@Test
@DisplayName("should throw exception for negative amount")
void shouldThrowExceptionForNegativeAmount() {
  assertThatThrownBy(() -> paymentService.processPayment(new BigDecimal("-10")))
    .isInstanceOf(InvalidPaymentException.class)
    .hasMessage("Payment amount must be positive");
}

@Test
@DisplayName("should throw NullPointerException for null user")
void shouldThrowForNullUser() {
  assertThatThrownBy(() -> userService.getFullName(null))
    .isInstanceOf(NullPointerException.class);
}
```

### Exception Messages

Assert exception messages for helpful context:

```java
assertThatThrownBy(() -> service.process(null))
  .isInstanceOf(ValidationException.class)
  .hasMessageContaining("cannot be null")
  .hasNoCause();
```

### Graceful Degradation

Test how the application handles errors:

```java
@Test
@DisplayName("should return fallback when database unavailable")
void shouldReturnFallbackWhenDatabaseUnavailable() {
  when(userRepository.findAll()).thenThrow(new DatabaseException("Connection refused"));

  final var result = userService.getAllUsersOrDefault();

  assertThat(result).isEmpty();
}
```

## JSpecify for Null Safety

Use JSpecify annotations in tests for clarity:

```java
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
class UserServiceUnTest {
  private UserService service;

  @Test
  @DisplayName("should handle null input gracefully")
  void shouldHandleNullInput() {
    final @Nullable String input = null;
    assertThat(service.process(input)).isEmpty();
  }

  @Test
  @DisplayName("should return non-null result for valid input")
  void shouldReturnNonNullForValidInput() {
    final var result = service.process("valid");
    assertThat(result).isNotNull();
  }
}
```

## Avoid Reflection in Unit Tests

Do not use reflection to test private methods. Test through public API or extract testable components:

```java
// Incorrect - using reflection
@Test
void testPrivateMethod() throws Exception {
  var method = MyClass.class.getDeclaredMethod("internalMethod", String.class);
  method.setAccessible(true);
  var result = method.invoke(myClass, "test");
  // ...
}

// Correct - test through public API
@Test
@DisplayName("should process input correctly")
void shouldProcessInputCorrectly() {
  // Test the public behavior that depends on the internal logic
  assertThat(myClass.publicMethod("test")).isEqualTo("expected");
}

// Correct - extract to testable component
@Test
@DisplayName("should validate email format")
void shouldValidateEmailFormat() {
  // EmailValidator extracted to separate testable class
  final var validator = new EmailValidator();
  assertThat(validator.isValid("test@example.com")).isTrue();
}
```

## Local Variable Declaration: `final var`

Use `final var` for local variables where the reference is not reassigned:

```java
// Correct
final var userService = new UserService(mockRepository);
final var result = userService.findById("user-1");
final var expected = Optional.of(new User("user-1"));

// Incorrect
UserService userService = new UserService(mockRepository);  // missing final
var result = userService.findById("user-1");  // missing final
```

Exception: If reassignment is required (loops, conditionals), omit `final` but keep `var`.

## Gotchas

- **Mockito `any()` matcher:** Use `any()` for loose matching, `eq()` for exact values when mixing with matchers
- **`@BeforeEach` runs before each `@Test` and `@ParameterizedTest`**: Reset mutable state here
- **Parameterized test method name:** Use `{index}` or `{arguments}` in `@DisplayName` for clarity
- **AssertJ vs JUnit assertions:** Don't mix styles; use AssertJ consistently
- **Mock injection:** `@InjectMocks` doesn't inject mocks into constructor parameters; prefer manual construction
- **Test method order:** Never rely on test execution order; tests run in arbitrary order
- **Static state:** Be careful with static state in tests; it persists across test methods
- **Mockito static mocks:** Use `Mockito.mockStatic` for static method mocking, requires cleanup

## Constraints

- **DO NOT** use reflection to test private or package-private methods
- **DO NOT** create tests that depend on execution order
- **DO NOT** use JUnit 4 annotations (`@Before`, `@After`, `@Ignore`)
- **DO NOT** use wildcard imports; explicitly list every import
- **DO NOT** make test classes or methods `public`
- **DO NOT** mix JUnit assertions with AssertJ; use AssertJ consistently

## Validation

After creating or modifying tests, verify:

1. Tests compile: `./gradlew :<module>:compileTestJava`
2. Tests pass: `./gradlew :<module>:test --tests "*UnTest"`
3. Test class follows `<ClassName>UnTest` naming convention
4. Test class has `@NullMarked` annotation
5. Test class and methods are package-private
6. All tests use AssertJ assertions (`assertThat`, `assertThatThrownBy`)
7. Each test follows Given-When-Then structure
8. Each test has `@DisplayName` with natural language description
9. Dependencies are properly mocked with Mockito
10. Parameterized tests are used for data variations
11. No reflection is used to access private members
