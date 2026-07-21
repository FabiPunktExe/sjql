package de.fabiexe.sjql;

import de.fabiexe.sjql.column.*;
import de.fabiexe.sjql.row.BasicWritableRow;
import de.fabiexe.sjql.row.ConstructorRowMapper;
import de.fabiexe.sjql.row.ReadableRow;
import de.fabiexe.sjql.row.WritableRow;
import org.jspecify.annotations.Nullable;

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This class represents a database table. It provides methods to define columns, insert data, and perform queries.
 *
 * @param <T> The type of the objects that represent rows in this table.
 */
public class Table<T extends @Nullable Object> {
    private final Class<T> type;
    private final String name;
    private final List<Column<?>> columns = new ArrayList<>();
    private @Nullable Function<ReadableRow, T> rowMapper;

    /**
     * Creates a new table with the given name, type, and row mapper.
     *
     * @param name The name of the table
     * @param type The class of the objects that represent rows in this table
     * @param rowMapper A function that maps a readable row to an object of type T.
     *                  If {@code null}, a default constructor-based mapper will be used.
     */
    public Table(
            Class<T> type,
            String name,
            @Nullable Function<ReadableRow, T> rowMapper
    ) {
        this.type = type;
        this.name = name;
        this.rowMapper = rowMapper;
    }

    /**
     * Creates a new table with the given name and type. A default constructor-based row mapper will be used.
     *
     * @param name The name of the table
     * @param type The class of the objects that represent rows in this table
     */
    public Table(Class<T> type, String name) {
        this(type, name, null);
    }

    /**
     * Adds a column to this table.
     *
     * @param <U> The type of the column
     * @param column The column to add
     * @return The added column
     * @throws IllegalArgumentException If a column with the same name already exists in this table
     */
    public <U extends @Nullable Object> Column<U> column(Column<U> column) {
        if (columns.stream().map(Column::name).anyMatch(Predicate.isEqual(column.name()))) {
            throw new IllegalArgumentException("Column " + column.name() + " already exists in table " + name);
        }
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
    public Column<@Nullable Integer> intColumn(String name) {
        return column(new IntColumn(this, name));
    }

    /**
     * Adds a double column to this table.
     *
     * @param name The name of the column
     * @return The added column
     */
    public Column<@Nullable Double> doubleColumn(String name) {
        return column(new DoubleColumn(this, name));
    }

    /**
     * Adds a string column to this table.
     *
     * @param name The name of the column
     * @param length The maximum length of the string
     * @return The added column
     */
    public Column<@Nullable String> stringColumn(String name, int length) {
        return column(new StringColumn(this, name, length));
    }

    /**
     * Adds a long column to this table.
     *
     * @param name The name of the column
     * @return The added column
     */
    public Column<@Nullable Long> longColumn(String name) {
        return column(new LongColumn(this, name));
    }

    /**
     * Adds a float column to this table.
     *
     * @param name The name of the column
     * @return The added column
     */
    public Column<@Nullable Float> floatColumn(String name) {
        return column(new FloatColumn(this, name));
    }

    /**
     * Adds a boolean column to this table.
     *
     * @param name The name of the column
     * @return The added column
     */
    public Column<@Nullable Boolean> booleanColumn(String name) {
        return column(new BooleanColumn(this, name));
    }

    /**
     * Adds a UUID column to this table.
     *
     * @param name The name of the column
     * @return The added column
     */
    public Column<@Nullable UUID> uuidColumn(String name) {
        return column(new UUIDColumn(this, name));
    }

    /**
     * Adds a timestamp column to this table.
     *
     * @param name The name of the column
     * @return The added column
     */
    public Column<@Nullable Instant> timestampColumn(String name) {
        return column(new TimestampColumn(this, name));
    }

    /**
     * Inserts a new row into this table. This method may only be used inside a transaction.
     *
     * @param builder A consumer that builds the row to be inserted
     * @throws IllegalStateException If this method is not called inside a transaction
     * @throws SQLException If an SQL error occurs during insertion
     * @see Database#transaction(Runnable)
     */
    public void insert(Consumer<WritableRow> builder) throws SQLException {
        if (!Database.CURRENT_DATABASE.isBound()) {
            throw new IllegalStateException("This method can only be called inside a transaction");
        }
        WritableRow row = new BasicWritableRow<>(this);
        builder.accept(row);
        Database.CURRENT_DATABASE.get().insert(this, row);
    }

    /**
     * Deletes rows from this table. This method may only be used inside a transaction.
     *
     * @return A {@link DeleteStatement} that can be used to specify and execute the delete operation
     * @throws IllegalStateException If this method is not called inside a transaction
     * @see Database#transaction(Runnable)
     */
    public DeleteStatement delete() {
        if (!Database.CURRENT_DATABASE.isBound()) {
            throw new IllegalStateException("This method can only be called inside a transaction");
        }
        return Database.CURRENT_DATABASE.get().delete(this);
    }

    /**
     * Updates rows in this table. This method may only be used inside a transaction.
     *
     * @param builder A consumer that defines how to build the update values for each row.
     *                It receives a {@link WritableRow} that can be used to set the new values for the columns.
     * @return An {@link UpdateStatement} that can be used to specify and execute the update operation
     * @throws IllegalStateException If this method is not called inside a transaction
     * @see Database#transaction(Runnable)
     */
    public UpdateStatement update(Consumer<WritableRow> builder) {
        if (!Database.CURRENT_DATABASE.isBound()) {
            throw new IllegalStateException("This method can only be called inside a transaction");
        }
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
     * @throws IllegalStateException If this method is not called inside a transaction
     * @see Database#transaction(Runnable)
     */
    public <U extends @Nullable Object> UpdateStatement update(Column<U> column, U value) {
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
     * @throws IllegalStateException If this method is not called inside a transaction
     * @see Database#transaction(Runnable)
     */
    public <U extends @Nullable Object, V extends @Nullable Object> UpdateStatement update(
            Column<U> column1, U value1,
            Column<V> column2, V value2
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
     * @throws IllegalStateException If this method is not called inside a transaction
     * @see Database#transaction(Runnable)
     */
    public <U extends @Nullable Object, V extends @Nullable Object, W extends @Nullable Object> UpdateStatement update(
            Column<U> column1, U value1,
            Column<V> column2, V value2,
            Column<W> column3, W value3
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
     * @throws IllegalStateException If this method is not called inside a transaction
     * @see Database#transaction(Runnable)
     */
    public Query<List<T>> select() {
        if (!Database.CURRENT_DATABASE.isBound()) {
            throw new IllegalStateException("This method can only be called inside a transaction");
        }
        return Database.CURRENT_DATABASE.get().select(this);
    }

    /**
     * Counts the number of rows in this table. This method may only be used inside a transaction.
     *
     * @return The number of rows in this table
     * @throws IllegalStateException If this method is not called inside a transaction
     * @throws SQLException If a database error occurs
     * @see Database#transaction(Runnable)
     */
    public long count() throws SQLException {
        return select().count().executeNotNull();
    }

    /**
     * Gets the class of the objects that represent rows in this table.
     *
     * @return The class of the objects that represent rows in this table
     */
    public Class<T> getType() {
        return type;
    }

    /**
     * Gets the name of this table.
     *
     * @return The name of this table
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the function that maps a readable row to an object of type {@code T}. If no custom row mapper was provided,
     * a default constructor-based mapper will be created and returned.
     *
     * @return The function that maps a readable row to an object of type {@code T}
     */
    public Function<ReadableRow, T> getRowMapper() {
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
    public List<Column<?>> getColumns() {
        return columns;
    }

    /**
     * Checks if this table has the given column.
     *
     * @param column The column to check
     * @return {@code true} if this table has the given column, {@code false} otherwise
     */
    public boolean hasColumn(Column<?> column) {
        return columns.contains(column);
    }
}