package de.fabiexe.sjql;

import de.fabiexe.sjql.column.*;
import de.fabiexe.sjql.row.BasicWritableRow;
import de.fabiexe.sjql.row.ConstructorRowMapper;
import de.fabiexe.sjql.row.ReadableRow;
import de.fabiexe.sjql.row.WritableRow;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * This class represents a database table. It provides methods to define columns, insert data, and perform queries.
 *
 * @param <T> The type of the objects that represent rows in this table.
 */
public class Table<T> {
    private final String name;
    private final Class<T> type;
    private final List<Column<?>> columns = new ArrayList<>();
    private Function<ReadableRow<T>, T> rowMapper;

    /**
     * Creates a new table with the given name, type, and row mapper.
     *
     * @param name The name of the table
     * @param type The class of the objects that represent rows in this table
     * @param rowMapper A function that maps a readable row to an object of type T.
     *                  If {@code null}, a default constructor-based mapper will be used.
     */
    public Table(
            @NotNull String name,
            @NotNull Class<T> type,
            @NotNull Function<ReadableRow<T>, T> rowMapper
    ) {
        this.name = name;
        this.type = type;
        this.rowMapper = rowMapper;
    }

    /**
     * Creates a new table with the given name and type. A default constructor-based row mapper will be used.
     *
     * @param name The name of the table
     * @param type The class of the objects that represent rows in this table
     */
    public Table(
            @NotNull String name,
            @NotNull Class<T> type
    ) {
        this.name = name;
        this.type = type;
        this.rowMapper = null;
    }

    /**
     * Adds a column to this table.
     *
     * @param <U> The type of the column
     * @param column The column to add
     * @return The added column
     */
    public <U> @NotNull Column<U> column(@NotNull Column<U> column) {
        columns.add(column);
        rowMapper = null;
        return column;
    }

    /**
     * Adds an integer column to this table.
     *
     * @param name The name of the column
     * @return The added column
     */
    public @NotNull Column<Integer> intColumn(@NotNull String name) {
        return column(new IntColumn(this, name));
    }

    /**
     * Adds a double column to this table.
     *
     * @param name The name of the column
     * @return The added column
     */
    public @NotNull Column<Double> doubleColumn(@NotNull String name) {
        return column(new DoubleColumn(this, name));
    }

    /**
     * Adds a string column to this table.
     *
     * @param name The name of the column
     * @param length The maximum length of the string
     * @return The added column
     */
    public @NotNull Column<String> stringColumn(@NotNull String name, int length) {
        return column(new StringColumn(this, name, length));
    }

    /**
     * Adds a long column to this table.
     *
     * @param name The name of the column
     * @return The added column
     */
    public @NotNull Column<Long> longColumn(@NotNull String name) {
        return column(new LongColumn(this, name));
    }

    /**
     * Adds a float column to this table.
     *
     * @param name The name of the column
     * @return The added column
     */
    public @NotNull Column<Float> floatColumn(@NotNull String name) {
        return column(new FloatColumn(this, name));
    }

    /**
     * Adds a boolean column to this table.
     *
     * @param name The name of the column
     * @return The added column
     */
    public @NotNull Column<Boolean> booleanColumn(@NotNull String name) {
        return column(new BooleanColumn(this, name));
    }

    /**
     * Adds a UUID column to this table.
     *
     * @param name The name of the column
     * @return The added column
     */
    public @NotNull Column<UUID> uuidColumn(@NotNull String name) {
        return column(new UUIDColumn(this, name));
    }

    /**
     * Adds a timestamp column to this table.
     *
     * @param name The name of the column
     * @return The added column
     */
    public @NotNull Column<Instant> timestampColumn(@NotNull String name) {
        return column(new TimestampColumn(this, name));
    }

    /**
     * Inserts a new row into this table. This method may only be used inside a transaction.
     *
     * @param builder A consumer that builds the row to be inserted
     * @throws SQLException If an SQL error occurs during insertion
     * @see Database#transaction(Runnable)
     */
    public void insert(@NotNull Consumer<WritableRow> builder) throws SQLException {
        WritableRow row = new BasicWritableRow<>(this);
        builder.accept(row);
        Database.CURRENT_DATABASE.get().insert(this, row);
    }

    /**
     * Deletes rows from this table. This method may only be used inside a transaction.
     *
     * @return A {@link DeleteStatement} that can be used to specify and execute the delete operation
     * @see Database#transaction(Runnable)
     */
    public @NotNull DeleteStatement delete() {
        return Database.CURRENT_DATABASE.get().delete(this);
    }

    /**
     * Updates rows in this table. This method may only be used inside a transaction.
     *
     * @param builder A consumer that defines how to build the update values for each row.
     *                It receives a {@link WritableRow} that can be used to set the new values for the columns.
     * @return An {@link UpdateStatement} that can be used to specify and execute the update operation
     * @see Database#transaction(Runnable)
     */
    public @NotNull UpdateStatement update(@NotNull Consumer<WritableRow> builder) {
        return Database.CURRENT_DATABASE.get().update(this, builder);
    }

    /**
     * Updates rows in this table by setting a single column to a new value.
     * This method may only be used inside a transaction.
     *
     * @param <U> The type of the column
     * @param column The column to update
     * @param value The new value for the column
     * @return An {@link UpdateStatement} that can be used to specify and execute the update operation
     * @see Database#transaction(Runnable)
     */
    public <U> @NotNull UpdateStatement update(@NotNull Column<U> column, U value) {
        return update(row -> row.set(column, value));
    }

    /**
     * Updates rows in this table by setting two columns to new values.
     * This method may only be used inside a transaction.
     *
     * @param <U> The type of the first column
     * @param <V> The type of the second column
     * @param column1 The first column to update
     * @param value1 The new value for the first column
     * @param column2 The second column to update
     * @param value2 The new value for the second column
     * @return An {@link UpdateStatement} that can be used to specify and execute the update operation
     * @see Database#transaction(Runnable)
     */
    public <U, V> @NotNull UpdateStatement update(
            @NotNull Column<U> column1, U value1,
            @NotNull Column<V> column2, V value2
    ) {
        return update(row -> {
            row.set(column1, value1);
            row.set(column2, value2);
        });
    }

    /**
     * Updates rows in this table by setting three columns to new values.
     * This method may only be used inside a transaction.
     *
     * @param <U> The type of the first column
     * @param <V> The type of the second column
     * @param <W> The type of the third column
     * @param column1 The first column to update
     * @param value1 The new value for the first column
     * @param column2 The second column to update
     * @param value2 The new value for the second column
     * @param column3 The third column to update
     * @param value3 The new value for the third column
     * @return An {@link UpdateStatement} that can be used to specify and execute the update operation
     * @see Database#transaction(Runnable)
     */
    public <U, V, W> @NotNull UpdateStatement update(
            @NotNull Column<U> column1, U value1,
            @NotNull Column<V> column2, V value2,
            @NotNull Column<W> column3, W value3
    ) {
        return update(row -> {
            row.set(column1, value1);
            row.set(column2, value2);
            row.set(column3, value3);
        });
    }

    /**
     * Creates a new query for selecting objects of type {@code T} from this table.
     *
     * @return A {@link Query} that can be used to specify and execute the selection operation
     */
    public @NotNull Query<List<T>> select() {
        return Database.CURRENT_DATABASE.get().select(this);
    }

    /**
     * Counts the number of rows in this table. This method may only be used inside a transaction.
     *
     * @return The number of rows in this table
     * @throws SQLException If a database error occurs
     * @see Database#transaction(Runnable)
     */
    public long count() throws SQLException {
        return select().count().executeNotNull();
    }

    /**
     * Gets the name of this table.
     *
     * @return The name of this table
     */
    public @NotNull String getName() {
        return name;
    }

    /**
     * Gets the class of the objects that represent rows in this table.
     *
     * @return The class of the objects that represent rows in this table
     */
    public @NotNull Class<T> getType() {
        return type;
    }

    /**
     * Gets the function that maps a readable row to an object of type {@code T}. If no custom row mapper was provided,
     * a default constructor-based mapper will be created and returned.
     *
     * @return The function that maps a readable row to an object of type {@code T}
     */
    public @NotNull Function<ReadableRow<T>, T> getRowMapper() {
        if (rowMapper == null) {
            rowMapper = new ConstructorRowMapper<>(type, columns.toArray(Column<?>[]::new));
        }
        return rowMapper;
    }

    /**
     * Gets the list of columns defined for this table.
     *
     * @return The list of columns defined for this table
     */
    public @NotNull List<Column<?>> getColumns() {
        return columns;
    }

    /**
     * Checks if this table has the given column.
     *
     * @param column The column to check
     * @return {@code true} if this table has the given column, {@code false} otherwise
     */
    public boolean hasColumn(@NotNull Column<?> column) {
        return columns.contains(column);
    }
}
