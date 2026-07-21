# SJQL (Simple Java Query Library) - AI agent context

## Project Overview
SJQL is a lightweight, type-safe Java/Kotlin library for interacting with SQL databases.  It aims to provide a simple, fluent API for common database operations without the overhead of heavy ORM frameworks.

Current version: `0.5.8` (see `build.gradle.kts`).

## Supported Databases
- H2
- SQLite
- PostgreSQL

`Database.create(DataSource)` detects the database from the JDBC URL and returns the matching implementation (`H2Database`, `SQLiteDatabase`, `PostgreSQLDatabase`). If the database is not supported, it throws `IllegalArgumentException`.

## Core Concepts

### 1. Table Definitions
Tables are typically defined as static constants within a Java record or class.
- Use `new Table<>(Record.class, "table_name")` to define a table.
- Use `new Table<>(Record.class, "table_name", rowMapper)` to provide a custom `Function<ReadableRow, T>` row mapper. If no mapper is given, a constructor-based mapper is generated automatically from the table columns.
- Define columns using methods like `table.stringColumn("name", length)`, `table.intColumn("name")`, `table.timestampColumn("name")`, etc.
- Mark one or multiple columns as primary key using `.primaryKey()`.
- Mark columns as required using `.notNull()` (`.primaryKey()` implicitly sets NOT NULL).
- Set default values using `.defaultValue(value)` or `.defaultValue(Expression)`.
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
- Operations like `insert`, `select`, `update`, and `delete` must be performed within a transaction block:
  - `database.transaction(Runnable action)`
  - `database.throwingTransaction(ThrowingRunnable<T> action)` — allows checked exceptions
  - `database.transaction(Supplier<T> action)` — returns a value
  - `database.throwingTransaction(ScopedValue.CallableOp<T, E> action)` — returns a value and allows checked exceptions
- Inside a transaction, you can call methods directly on the `Table` instance (e.g., `User.TABLE.select()`).
- `Database.create(DataSource)` throws `IllegalArgumentException` for unsupported databases and `SQLException` for connection errors.

### 3. Queries & Statements
- SJQL uses a fluent API for building queries.
- `select()` returns a `Query<List<T>>`.
- `delete()` returns a `DeleteStatement`.
- `update(Consumer<WritableRow> builder)` returns an `UpdateStatement`.
- Convenience overloads exist for updating one, two, or three columns directly: `update(Column, value)`, `update(Column1, value1, Column2, value2)`, etc.
- `count()` is available directly on `Table`.
- Query methods:
  - `where(Expression)` / `where(null)` for no condition
  - `orderBy(Expression, boolean)` and overloads for `Column`
  - `orderBy(List<Map.Entry<Expression, Boolean>>)` for multi-column ordering
  - `limit(Long)` / `limit(null)`
  - `count()` to return a `Query<Long>`
  - `execute()` returns the result (may be `null`)
  - `executeNotNull()` returns a non-null result or throws `NullPointerException`

### 4. Expressions
- Expressions are built via `Expression.constant(value)` or column helpers.
- Supported constant types: `null`, `Integer`, `Long`, `Float`, `Double`, `Boolean`, `String`, `UUID`, `Instant`, and Kotlin counterparts (`kotlin.uuid.Uuid`, `kotlin.time.Instant`).
- `Expression.currentTimestamp()` evaluates to the database's `CURRENT_TIMESTAMP`.
- Comparison operators on `Column` and `Expression`: `eq`, `neq`, `gt`, `gte`, `lt`, `lte`.
- Logical operators on `Expression`: `and`, `or`, `xor`, `not`.
- Null checks: `checkNull()` (`IS NULL`) and `checkNotNull()` (`IS NOT NULL`).

### 5. Primary Keys, Defaults & Nullability
- Use `.primaryKey()` to mark columns as primary keys. Primary keys are implicitly NOT NULL.
- Use `.notNull()` to mark columns as NOT NULL explicitly.
- During inserts, columns may be omitted if they have a default value, are auto-generated, or are nullable.
- SQL generation for `createTable` includes `NOT NULL` constraints and `DEFAULT` clauses.
- `insert` validates that NOT NULL columns are not set to `null` and that required columns are provided.

### 6. Kotlin Support
- Kotlin extension functions provide idiomatic infix syntax for comparisons and query building.
- `Column` infix operators: `eq`, `neq`, `gt`, `gte`, `lt`, `lte`.
- `Expression` infix operators: `eq`, `neq`, `gt`, `gte`, `lt`, `lte`, `and`, `or`, `xor`.
- `Query` extensions: infix `where`, `orderBy`, `limit`.
- Kotlin-specific column types:
  - `Table.kUuidColumn(name)` maps `kotlin.uuid.Uuid` to/from `java.util.UUID`.
  - `Table.kTimestampColumn(name)` maps `kotlin.time.Instant` to/from `java.time.Instant`.
- The Kotlin module is located in `src/main/kotlin/de/fabiexe/sjql/`.

## Architecture Details

- **Package Structure:**
  - `de.fabiexe.sjql`: Core interfaces and classes (`Table`, `Database`, `Query`, `Statement`, `DeleteStatement`, `UpdateStatement`).
  - `de.fabiexe.sjql.column`: Column definitions. `Column<T>` is a `sealed interface` permitting `PrimitiveColumn` and `ComplexColumn`.
  - `de.fabiexe.sjql.database`: Database-specific implementations (`BasicDatabase`, `H2Database`, `SQLiteDatabase`, `PostgreSQLDatabase`).
  - `de.fabiexe.sjql.expression`: `Expression` interface and operator API.
  - `de.fabiexe.sjql.expression.constant`: Constant value expressions (`IntExpression`, `StringExpression`, `UUIDExpression`, etc.).
  - `de.fabiexe.sjql.expression.dynamic`: Dynamic expressions (`ColumnExpression`, `CurrentTimestampExpression`).
  - `de.fabiexe.sjql.expression.logical`: Logical and comparison expressions (`AndExpression`, `EqualsExpression`, `IsNullExpression`, etc.).
  - `de.fabiexe.sjql.query`: Query and statement implementations (`BasicQuery`, `BasicValueQuery`, `BasicRowQuery`, `BasicCountQuery`, `BasicDeleteStatement`, `BasicUpdateStatement`, `ConstantQuery`).
  - `de.fabiexe.sjql.row`: Row mapping (`ReadableRow`, `WritableRow`, `BasicReadableRow`, `BasicWritableRow`, `ConstructorRowMapper`, `ColumnMapper`).
  - `de.fabiexe.sjql.util`: Utilities (`SQLUtil`, `ThrowingRunnable`, `ThrowingSupplier`).

- **Type Safety:** The library relies heavily on Java generics to ensure that column types match the expected Java types. All packages are annotated with JSpecify `@NullMarked` for explicit nullability.

- **Null Safety:** The project uses JSpecify annotations (`@Nullable`, `@NonNull`, `@NullMarked`). Kotlin callers use platform types accordingly.

- **SQL Generation:** `SQLUtil` is a utility class for generating parameterized SQL strings and setting JDBC parameters.

- **Column Types:**
  - Primitive columns stored directly in the database: `IntColumn`, `LongColumn`, `FloatColumn`, `DoubleColumn`, `BooleanColumn`, `StringColumn`, `UUIDColumn`, `TimestampColumn`.
  - `ComplexColumn<T, B>` maps a complex Java type `T` to a primitive base type `B` stored in the database. Used for Kotlin-specific types.

## Guidelines for AI Agents

1. **Consistent Style:** Follow the existing fluent API pattern and the project's Javadoc conventions.
2. **Nullability:** Use JSpecify annotations consistently. Mark nullable parameters and return values with `@Nullable`; use `@NonNull` where the type system needs narrowing (e.g., `Column<@NonNull T> primaryKey()`).
3. **New Column Types:** When adding primitive column types, extend `PrimitiveColumn` (which is a `sealed` class — update the permits list). For columns that map a complex type to a primitive database type, extend `ComplexColumn`.
4. **Database Support:** New database dialects should extend `BasicDatabase` and implement database-specific SQL in `getColumnType`, `isAutoGenerated`, and `getPrimaryKeyAddition`.
5. **Transactions:** Always assume that mutating database operations (insert, update, delete) require an active `CURRENT_DATABASE` in scope. Table-level methods verify this and throw `IllegalStateException` otherwise.
6. **Testing:** Use the existing test suite in `src/test/java/de/fabiexe/sjql/` as a reference for integration tests. `AbstractDatabaseTest` is the base class for database-specific tests.
7. **Kotlin Interop:** When adding operators or convenience methods, consider adding matching Kotlin extension functions (infix where appropriate) in the `src/main/kotlin` module.
