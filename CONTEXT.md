# SJQL (Simple Java Query Library) - AI agent context

## Project Overview
SJQL is a lightweight, type-safe Java library for interacting with SQL databases.  It aims to provide a simple, fluent API for common database operations without the overhead of heavy ORM frameworks.

## Supported Databases
- H2
- SQLite
- PostgreSQL

## Core Concepts

### 1. Table Definitions
Tables are typically defined as static constants within a Java class or record.
- Use `new Table<>(Record.class, "table_name")` to define a table.
- Define columns using methods like `table.stringColumn("name", length)`, `table.intColumn("name")`, `table.timestampColumn("name")`, etc.
- Mark one or multiple columns as primary key using `.primaryKey()`.
- Example:
  ```java
  public record User(String name, int age) {
      public static final Table<User> TABLE = new Table<>(User.class, "users");
      public static final Column<String> NAME = TABLE.stringColumn("name", 64).primaryKey();
      public static final Column<Integer> AGE = TABLE.intColumn("age");
  }
  ```
- Example (multiple primary keys):
  ```java
  public record UserRole(String username, String role) {
      public static final Table<UserRole> TABLE = new Table<>(UserRole.class, "user_roles");
      public static final Column<String> USERNAME = TABLE.stringColumn("username", 64).primaryKey();
      public static final Column<String> ROLE = TABLE.stringColumn("role", 64).primaryKey();
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

### 4. Primary Keys & Defaults
- Use `.primaryKey()` to mark columns as primary keys. Primary keys are implicitly NOT NULL.
- Use `.notNull()` to mark columns as NOT NULL.
- During inserts, columns may be omitted if they have a default value, are auto-generated, or are nullable (not marked as NOT NULL).
- SQL generation for `createTable` includes `NOT NULL` constraints for relevant columns.
- `insert` and `update` operations validate that `NOT NULL` columns are not set to `null`.

## Architecture Details

- **Package Structure:**
  - `de.fabiexe.sjql`: Core interfaces and classes (`Table`, `Database`, `Query`).
  - `de.fabiexe.sjql.column`: Specific column types (String, Int, Boolean, etc.).
  - `de.fabiexe.sjql.database`: Database-specific implementations (BasicDatabase, H2Database, SQLiteDatabase, PostgreSQLDatabase).
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
