# SJQL (Simple Java Query Library) - AI agent context

## Project Overview
SJQL is a lightweight, type-safe Java library for interacting with SQL databases.  It aims to provide a simple, fluent API for common database operations without the overhead of heavy ORM frameworks.

## Supported Databases
- H2
- SQLite

## Core Concepts

### 1. Table Definitions
Tables are typically defined as static constants within a Java class or record.
- Use `new Table<>("table_name", Record.class)` to define a table.
- Define columns using methods like `table.stringColumn("name", length)`, `table.intColumn("name")`, etc.
- Example:
  ```java
  public record User(String name, int age) {
      public static final Table<User> TABLE = new Table<>("users", User.class);
      public static final Column<String> NAME = TABLE.stringColumn("name", 64);
      public static final Column<Integer> AGE = TABLE.intColumn("age");
  }
  ```

### 2. Database & Transactions
- The `Database` interface is the entry point.
- It uses `ScopedValue<Database> CURRENT_DATABASE` to manage context.
- Operations like `insert`, `select`, and `delete` should be performed within a transaction block:
  - `database.transaction(() -> { ... })`
  - `database.throwingTransaction(() -> { ... })`
- Inside a transaction, you can call methods directly on the `Table` instance (e.g., `User.TABLE.select()`).

### 3. Query & Statements
- SJQL uses a fluent API for building queries.
- `select()` returns a `Query<List<T>>`.
- `delete()` returns a `DeleteStatement`.
- `update(Consumer<WritableRow> builder)` returns an `UpdateStatement`.
- Support for `where()` clauses using expressions (e.g., `Column.eq(value)`, `Column.gt(value)`).

## Architecture Details

- **Package Structure:**
  - `de.fabiexe.sjql`: Core interfaces and classes (`Table`, `Database`, `Query`).
  - `de.fabiexe.sjql.column`: Specific column types (String, Int, Boolean, etc.).
  - `de.fabiexe.sjql.database`: Database-specific implementations (BasicDatabase, H2Database, ...).
  - `de.fabiexe.sjql.expression`: Logic for SQL expressions and operators.
  - `de.fabiexe.sjql.row`: Mapping between SQL results and Java objects.

- **Type Safety:** The library relies heavily on Java generics to ensure that column types match the expected Java types.

- **SQL Generation:** `SQLUtil` is a utility class for generating SQL strings.

## Guidelines for AI Agents

1. **Consistent Style:** Follow the existing fluent API pattern.
2. **New Column Types:** When adding column types, ensure they extend `BasicColumn` and have a corresponding `Expression` type.
3. **Database Support:** New database dialects should implement the `Database` interface and handle specific SQL syntax in `BasicDatabase` or a subclass.
4. **Transactions:** Always assume that database operations (select, insert, delete) require an active `CURRENT_DATABASE` in scope.
5. **Testing:** Use the existing test suite in `src/test/java/de/fabiexe/sjql/database/` as a reference for integration tests.
