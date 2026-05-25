---
name: commit
description: Use when you need to create conventional commits grouped by related changes logically.
---

# AI Skill — Professional Git Commit Message Generator

## Overview

This skill defines the rules, conventions, structure, and best practices for generating professional Git commit messages following:

- Conventional Commits
- Enterprise development standards
- Clean architecture naming conventions
- Clear and concise technical communication

The objective of this skill is to generate commit messages that are:

- Readable
- Consistent
- Technically accurate
- Useful for changelogs and PR reviews
- Easy to scan in Git history

---

# Commit Structure

A commit must follow this structure:

```text
<type>(<scope>): <short description>

- Detailed change 1
- Detailed change 2
- Detailed change 3
```

---

# Conventional Commit Types

| Type | Usage |
|---|---|
| feat | New functionality |
| fix | Bug fixes |
| refactor | Internal code improvements without behavior changes |
| chore | Configuration, dependencies, maintenance |
| docs | Documentation changes |
| test | Tests additions or improvements |
| build | Build system or dependency changes |
| ci | CI/CD pipeline changes |
| perf | Performance improvements |
| style | Formatting or style-only changes |

---

# Scope Naming Rules

The scope should:

- Be lowercase
- Use kebab-case
- Represent the affected module or domain
- Be short and meaningful

## Good Examples

```text
feat(users): add user search endpoint
fix(retainage-assets): correct reserve calculation
refactor(past-due-fee): simplify search queries
chore(build): update Gradle configuration
```

## Bad Examples

```text
feat(UserModule)
fix(RETAINAGE)
refactor(my-super-large-module-name)
```

---

# Header Rules

## Good Practices

- Use imperative tone
- Keep it concise
- Focus on the business or technical intent
- Avoid unnecessary implementation details
- Start with a verb

## Recommended Verbs

| Preferred | Avoid |
|---|---|
| add | added |
| update | updated |
| fix | solved |
| refactor | improve |
| remove | deleted |
| rename | changed name |
| implement | make |
| optimize | enhance |

---

# Body Rules

The body should:

- Explain relevant technical changes
- Use bullet points
- Use past tense consistently
- Avoid repetition
- Focus on meaningful changes only

---

# Formatting Rules

## Use backticks for:

- Classes
- Methods
- Variables
- Tables
- Endpoints
- Configuration fields

### Example

```text
- Added `UserController`
- Updated `application.yml`
- Added `retainage_amount_provided` table
```

---

# Best Practices

## 1. Keep commits atomic

A commit should represent a single logical change.

### Good

```text
feat(users): add user role endpoint
```

### Bad

```text
feat(users): add endpoints and fix Docker and update README and improve tests
```

---

## 2. Describe intent, not implementation noise

### Good

```text
fix(retainage-assets): prevent inconsistent reserve updates
```

### Bad

```text
fix(retainage-assets): change line 45 and update if condition
```

---

## 3. Avoid vague descriptions

### Bad

```text
fix(users): fix errors
```

### Good

```text
fix(users): validate duplicated usernames before creation
```

---

## 4. Avoid redundant wording

### Bad

```text
feat(users): add new user feature
```

### Better

```text
feat(users): add user creation endpoint
```

---

# Recommended Commit Style

## Feature Example

```text
feat(users): implement REST API for user management

- Added `UserController` with CRUD endpoints
- Added DTOs for user requests and responses
- Implemented user service operations
- Added OpenAPI documentation
```

---

## Fix Example

```text
fix(retainage-assets): correct retainage reserve calculation

- Fixed reserve adjustment calculation for disputed assets
- Prevented duplicated retainage cash release
- Updated reserve summary totals
```

---

## Refactor Example

```text
refactor(past-due-fee): simplify search query implementation

- Replaced large JPQL query with Specifications
- Introduced `PastDueFeeSearchCriteria`
- Removed unnecessary cast operations
```

---

## Chore Example

```text
chore(build): update Gradle module configuration

- Updated Gradle Kotlin DSL configuration
- Added Checkstyle configuration
- Refactored module package structure
```

---

# Enterprise Backend Guidelines

## Prefer these terms

| Preferred | Avoid |
|---|---|
| retrieve | get |
| persist | save |
| remove | delete |
| resolve | solve |
| expose | show |
| process | handle |
| validate | check |
| optimize | improve |

---

# Recommended Language Style

## Use concise technical English

### Good

```text
- Added validation for duplicated invoices
```

### Bad

```text
- Added a validation process that checks if invoices are duplicated
```

---

# REST API Commit Conventions

## Endpoints

Use:

```text
- Added endpoint to retrieve users
- Added endpoint to execute external payments
```

Avoid:

```text
- Added GET endpoint
- Added controller methods
```

---

# Database Commit Conventions

## Good Examples

```text
- Added `retainage_reserve_adjustment_assets` table
- Updated composite primary key in `dates_info`
- Added index for provider search
```

---

# Refactor Commit Conventions

A refactor commit should:

- Preserve behavior
- Improve readability or architecture
- Reduce complexity
- Clarify responsibilities

## Good Example

```text
refactor(users): separate persistence and web layers
```

---

# Testing Commit Conventions

## Good Examples

```text
test(users): add unit tests for user services
```

```text
test(auth): add integration tests for JWT authentication
```

---

# Documentation Commit Conventions

## Good Examples

```text
docs(users): add JavaDoc to service layer
```

```text
docs(openapi): update API documentation
```

---

# Anti-Patterns

Avoid these commit messages:

```text
fix bug
small changes
update stuff
improve code
wip
changes
misc fixes
```

---

# Recommended AI Behavior

When generating commits, the AI should:

1. Detect the real intention of the change
2. Use the correct Conventional Commit type
3. Use meaningful scopes
4. Prefer concise technical language
5. Group related changes logically
6. Group changes into meaningful semantic commits
7. Remove implementation noise
8. Correct grammar and technical wording
9. Standardize formatting
10. Avoid redundancy
11. Prioritize clarity over verbosity

---

# Example Input → Output

## Input

```text
Added user endpoints and fixed duplicated username validation
```

## Output

```text
feat(users): add user management endpoints

- Added REST endpoints for user operations
- Added validation for duplicated usernames
```

---

# Final Recommendation

A good commit message should answer:

- What changed?
- Why was it changed?
- What is the functional or technical impact?

Without unnecessary implementation details.

