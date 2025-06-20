# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository Overview

This is the Komapper Examples repository demonstrating various ways to use Komapper, a Kotlin SQL Mapper library. The repository contains 10 example projects showcasing different frameworks and database access patterns.

- **Komapper Version**: 5.3.0
- **Kotlin Version**: 2.1.21
- **Java Target**: 17
- **Build Tool**: Gradle 8.14.2 with Kotlin DSL

## Common Development Commands

### Building and Testing
```bash
# Build all projects
./gradlew build

# Build specific project
./gradlew :<project-name>:build

# Run all tests
./gradlew test

# Run tests for specific project
./gradlew :<project-name>:test

# Run a single test class
./gradlew :<project-name>:test --tests "org.komapper.example.ApplicationTest"
```

### Running Applications

**Spring Boot Applications:**
```bash
./gradlew :spring-boot-jdbc:bootRun
./gradlew :spring-boot-r2dbc:bootRun
./gradlew :jpetstore:bootRun  # Access at http://localhost:8080/ (user: jpetstore/jpetstore)
```

**Console Applications:**
```bash
./gradlew :console-jdbc:run
./gradlew :console-r2dbc:run
```

**Ktor Application:**
```bash
./gradlew :kweet:run  # Access at http://localhost:8080/
```

**Quarkus Application:**
```bash
./gradlew :quarkus-jdbc:quarkusDev  # Development mode with hot reload
```

### Code Formatting
```bash
# Check formatting
./gradlew spotlessCheck

# Auto-format code (automatically runs before build)
./gradlew spotlessApply
```

## Project Structure and Architecture

### Multi-Module Structure
Each example is a separate Gradle subproject demonstrating specific Komapper features:

- **codegen**: KSP code generation examples
- **comparison-with-exposed**: Comparison with Exposed ORM
- **console-jdbc/r2dbc**: Simple console applications
- **spring-boot-jdbc/r2dbc**: Spring Boot integrations
- **jpetstore**: Full e-commerce application (Spring Boot)
- **kweet**: Twitter-like messaging app (Ktor)
- **quarkus-jdbc**: Quarkus framework integration
- **repository-pattern-jdbc**: Repository pattern implementation

### Key Architectural Patterns

1. **Entity Mapping**: All projects use Komapper's annotation-based entity mapping
   - Entities are in `entity` packages
   - Use `@KomapperEntity` annotation
   - Primary keys use `@KomapperId`

2. **Database Configuration**: 
   - JDBC projects use `JdbcDatabase` 
   - R2DBC projects use `R2dbcDatabase`
   - Most examples use H2 in-memory database
   - Configuration typically in `application.yml` or `application.properties`

3. **Transaction Management**:
   - Spring Boot: Declarative with `@Transactional` or programmatic with `TransactionOperator`
   - Console/Ktor: Manual transaction management with `db.withTransaction`

4. **Code Generation**: Uses KSP (Kotlin Symbol Processing) to generate metamodel classes for type-safe queries

### Dependencies and Versions
All dependency versions are centralized in `gradle/libs.versions.toml`. Key frameworks:
- Spring Boot: 3.5.3
- Quarkus: 3.24.0
- Ktor: 3.2.0
- Testing: JUnit 5, Testcontainers

### Important Files
- **Root build configuration**: `build.gradle.kts`
- **Dependency versions**: `gradle/libs.versions.toml`
- **CI/CD workflow**: `.github/workflows/build.yml`
- **Auto-update config**: `renovate.json`