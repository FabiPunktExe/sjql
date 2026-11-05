# SJQL (Simple Java Query Library) - AI agent context

## Project Overview
SJQL is a lightweight, type-safe Java/Kotlin library for interacting with SQL databases.  It aims to provide a simple, fluent API for common database operations without the overhead of heavy ORM frameworks.

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

### 4. Combining Tables (Implicit Joins)
- Multiple tables can be combined in one query without SQL `JOIN` syntax: they are listed comma-separated in `FROM`, and the join condition is expressed in `WHERE`.
- `Table.ref()` returns an unaliased `TableRef<T>`; `Table.alias("c")` returns an aliased one.
- `TableRef.column(Column)` returns a `ColumnRef<T>`, which is an `Expression` and renders qualified as `"alias"."column"`.
- `TableRef.selectRows(otherRef, ...)` (or `Database.selectRows(TableRef, TableRef...)`) returns a `Query<List<ReadableRow>>`.
- `ReadableRow.get(ColumnRef)` reads a qualified value. `ReadableRow.get(Column)` works when the column belongs to exactly one of the referenced tables; it throws on ambiguity (self-joins require `ColumnRef`).
- Plain `Column` expressions are automatically qualified when the query has an alias or more than one table, as long as the owning table is unique. Existing single-table, unaliased queries keep rendering unqualified columns.
- Aliases must be unique per query; duplicate qualifiers are rejected with `IllegalArgumentException`.
- Example:
  ```java
  TableRef<Coffee> c = Coffee.TABLE.alias("c");
  TableRef<Purchase> p = Purchase.TABLE.alias("p");
  List<ReadableRow> rows = c.selectRows(p)
          .where(c.column(Coffee.NAME).eq(p.column(Purchase.COFFEE_NAME)))
          .executeNotNull();
  // SELECT * FROM "coffee" AS "c", "purchase" AS "p" WHERE ("c"."name") = ("p"."coffee_name")
  String name = rows.getFirst().get(c.column(Coffee.NAME));
  ```

### 5. Expressions
- Expressions are built via `Expression.constant(value)` or column helpers.
- Supported constant types: `null`, `Integer`, `Long`, `Float`, `Double`, `Boolean`, `String`, `UUID`, `Instant`, and Kotlin counterparts (`kotlin.uuid.Uuid`, `kotlin.time.Instant`).
- `Expression.currentTimestamp()` evaluates to the database's `CURRENT_TIMESTAMP`.
- Comparison operators on `Column` and `Expression`: `eq`, `neq`, `gt`, `gte`, `lt`, `lte`.
- Logical operators on `Expression`: `and`, `or`, `xor`, `not`.
- Null checks: `checkNull()` (`IS NULL`) and `checkNotNull()` (`IS NOT NULL`).

### 6. Primary Keys, Unique Keys, Defaults & Nullability
- Use `.primaryKey()` to mark columns as primary keys. Primary keys are implicitly NOT NULL.
- Use `.unique()` to mark a single column as a unique key.
- Use `Table.unique(Column1, Column2, ...)` to define a composite unique key spanning multiple columns.
- Use `.notNull()` to mark columns as NOT NULL explicitly.
- During inserts, columns may be omitted if they have a default value, are auto-generated, or are nullable.
- SQL generation for `createTable` includes `UNIQUE`, `NOT NULL` constraints and `DEFAULT` clauses.
- `insert` validates that NOT NULL columns are not set to `null` and that required columns are provided.
- `Table.insertReturning(Column, Consumer<WritableRow>)` inserts a row and returns the value of the given column for the inserted row. This includes database-generated values (e.g. auto-increment primary keys) and column defaults. `Database.insertReturning(Table, WritableRow, Column)` is the corresponding database-level operation.
- `insertReturning` is implemented with `INSERT ... RETURNING` for SQLite and PostgreSQL and with `SELECT ... FROM FINAL TABLE (INSERT ...)` for H2 (`BasicDatabase.buildReturningSql`).

### 7. Kotlin Support
- Kotlin extension functions provide idiomatic infix syntax for comparisons and query building.
- `Column` infix operators: `eq`, `neq`, `gt`, `gte`, `lt`, `lte`.
- `Expression` infix operators: `eq`, `neq`, `gt`, `gte`, `lt`, `lte`, `and`, `or`, `xor`.
- `Query` extensions: infix `where`, `orderBy`, `limit`.
- `TableRef` operator: `tableRef[column]` returns a `ColumnRef<T>`.
- Kotlin-specific column types:
  - `Table.kUuidColumn(name)` maps `kotlin.uuid.Uuid` to/from `java.util.UUID`.
  - `Table.kTimestampColumn(name)` maps `kotlin.time.Instant` to/from `java.time.Instant`.
- The Kotlin module is located in `sjql-kotlin/src`; the Java core is in `sjql/src`.

## Architecture Details

- **Package Structure:**
  - `de.fabiexe.sjql`: Core interfaces and classes (`Table`, `TableRef`, `Database`, `Query`, `Statement`, `DeleteStatement`, `UpdateStatement`).
  - `de.fabiexe.sjql.column`: Column definitions. `Column<T>` is a `sealed interface` permitting `PrimitiveColumn` and `ComplexColumn`; `ColumnRef<T>` is a qualified column reference.
  - `de.fabiexe.sjql.database`: Database-specific implementations (`BasicDatabase`, `H2Database`, `SQLiteDatabase`, `PostgreSQLDatabase`).
  - `de.fabiexe.sjql.expression`: `Expression` interface and operator API.
  - `de.fabiexe.sjql.expression.constant`: Constant value expressions (`IntExpression`, `StringExpression`, `UUIDExpression`, etc.).
  - `de.fabiexe.sjql.expression.dynamic`: Dynamic expressions (`ColumnExpression`, `CurrentTimestampExpression`).
  - `de.fabiexe.sjql.expression.logical`: Logical and comparison expressions (`AndExpression`, `EqualsExpression`, `IsNullExpression`, etc.).
  - `de.fabiexe.sjql.query`: Query and statement implementations (`BasicQuery`, `BasicValueQuery`, `BasicRowQuery`, `BasicCountQuery`, `BasicDeleteStatement`, `BasicUpdateStatement`, `ConstantQuery`).
  - `de.fabiexe.sjql.row`: Row mapping (`ReadableRow`, `WritableRow`, `BasicReadableRow`, `BasicMultiTableRow`, `BasicWritableRow`, `ConstructorRowMapper`, `ColumnMapper`).
  - `de.fabiexe.sjql.util`: Utilities (`SQLUtil`, `ThrowingRunnable`, `ThrowingSupplier`).

- **Type Safety:** The library relies heavily on Java generics to ensure that column types match the expected Java types. All packages are annotated with JSpecify `@NullMarked` for explicit nullability.

- **Null Safety:** The project uses JSpecify annotations (`@Nullable`, `@NonNull`, `@NullMarked`). Kotlin callers use platform types accordingly.

- **SQL Generation:** `SQLUtil` is a utility class for generating parameterized SQL strings and setting JDBC parameters. `buildFromClause` renders the comma-separated `FROM` list, and the table-aware `buildSql` overload qualifies plain columns automatically.

- **Column Types:**
  - Primitive columns stored directly in the database: `IntColumn`, `LongColumn`, `FloatColumn`, `DoubleColumn`, `BooleanColumn`, `StringColumn`, `UUIDColumn`, `TimestampColumn`.
  - `ComplexColumn<T, B>` maps a complex Java type `T` to a primitive base type `B` stored in the database. Used for Kotlin-specific types.

## Guidelines for AI Agents

1. **Consistent Style:** Follow the existing fluent API pattern and the project's Javadoc conventions.
2. **Nullability:** Use JSpecify annotations consistently. Mark nullable parameters and return values with `@Nullable`; use `@NonNull` where the type system needs narrowing (e.g., `Column<@NonNull T> primaryKey()`).
3. **New Column Types:** When adding primitive column types, extend `PrimitiveColumn` (which is a `sealed` class — update the permits list). For columns that map a complex type to a primitive database type, extend `ComplexColumn`.
4. **Database Support:** New database dialects should extend `BasicDatabase` and implement database-specific SQL in `getColumnType`, `isAutoGenerated`, and `getPrimaryKeyAddition`.
5. **Transactions:** Always assume that mutating database operations (insert, update, delete) require an active `CURRENT_DATABASE` in scope. Table-level methods verify this and throw `IllegalStateException` otherwise.
6. **Testing:** Use the existing test suite in `test` as a reference for integration tests. `AbstractDatabaseTest` is the base class for database-specific tests.
