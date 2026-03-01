package de.fabiexe.sjql;

import de.fabiexe.sjql.database.H2Database;
import de.fabiexe.sjql.database.PostgreSQLDatabase;
import de.fabiexe.sjql.database.SQLiteDatabase;
import de.fabiexe.sjql.row.ReadableRow;
import de.fabiexe.sjql.row.WritableRow;
import de.fabiexe.sjql.util.ThrowingRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Database {
    /** A scoped value that holds the current {@link Database} instance for the duration of a transaction. */
    ScopedValue<Database> CURRENT_DATABASE = ScopedValue.newInstance();

    /**
     * Creates a new table in the database based on the given {@link Table} definition.
     * If the table already exists, no action is taken.
     *
     * @param table The table definition to create
     * @throws SQLException If a database error occurs
     */
    void createTable(@NotNull Table<?> table) throws SQLException;

    /**
     * Deletes the specified table from the database. If the table does not exist, no action is taken.
     *
     * @param table The table to delete
     * @throws SQLException If a database error occurs
     */
    void deleteTable(@NotNull Table<?> table) throws SQLException;

    /**
     * Checks if the specified table exists in the database.
     *
     * @param table The table to check for existence
     * @return {@code true} if the table exists, {@code false} otherwise
     * @throws SQLException If a database error occurs
     */
    boolean tableExists(@NotNull Table<?> table) throws SQLException;

    /**
     * Inserts a new row into the specified table.
     *
     * @param <T> The type of the objects that represent rows in the table
     * @param table The table to insert the row into
     * @param row The row data to insert
     * @throws SQLException If a database error occurs
     */
    <T> void insert(@NotNull Table<T> table, @NotNull WritableRow row) throws SQLException;

    /**
     * Deletes rows from the specified table.
     *
     * @param <T> The type of the objects that represent rows in the table
     * @param table The table to delete rows from
     * @return A {@link DeleteStatement} that can be used to specify and execute the operation
     */
    <T> @NotNull DeleteStatement delete(@NotNull Table<T> table);

    /**
     * Updates rows in the specified table based on the given condition and update values.
     *
     * @param <T> The type of the objects that represent rows in the table
     * @param table The table to update rows in
     * @param builder A consumer that defines how to build the update values for each row.
     *                It receives a {@link WritableRow} that can be used to set the new values for the columns.
     * @return An {@link UpdateStatement} that can be used to specify and execute the operation
     */
    <T> @NotNull UpdateStatement update(@NotNull Table<T> table, @NotNull Consumer<WritableRow> builder);

    /**
     * Creates a new {@link Query} for selecting rows from the specified table.
     *
     * @param <T> The type of the objects that represent rows in the table
     * @param table The table to select rows from
     * @return A {@link Query} that can be used to specify and execute the selection operation
     */
    <T> @NotNull Query<List<ReadableRow<T>>> selectRows(@NotNull Table<T> table);

    /**
     * Creates a new {@link Query} for selecting objects of type {@code T} from the specified table.
     *
     * @param <T> The type of the objects that represent rows in the table
     * @param table The table to select objects from
     * @return A {@link Query} that can be used to specify and execute the selection operation
     */
    <T> @NotNull Query<List<T>> select(@NotNull Table<T> table);

    /**
     * Executes the given action within a transaction.
     *
     * @param action The action to execute within the transaction
     */
    default void transaction(@NotNull Runnable action) {
        ScopedValue.where(CURRENT_DATABASE, this).run(action);
    }

    /**
     * Executes the given action within a transaction, allowing it to throw a checked exception.
     *
     * @param <T> The type of the exception that may be thrown by the action
     * @param action The action to execute within the transaction
     * @throws T If the action throws an exception of type T
     */
    default <T extends Throwable> void throwingTransaction(@NotNull ThrowingRunnable<T> action) throws T {
        ScopedValue.where(CURRENT_DATABASE, this).call(() -> {
            action.run();
            return null;
        });
    }

    /**
     * Executes the given action within a transaction and returns its result.
     *
     * @param <T> The type of the result returned by the action
     * @param action The action to execute within the transaction
     * @return The result of the action
     */
    default <T> T transaction(@NotNull Supplier<T> action) {
        return ScopedValue.where(CURRENT_DATABASE, this).call(action::get);
    }

    /**
     * Executes the given action within a transaction, allowing it to throw a checked exception, and returns its result.
     *
     * @param <T> The type of the result returned by the action
     * @param <E> The type of the exception that may be thrown by the action
     * @param action The action to execute within the transaction
     * @return The result of the action
     * @throws E If the action throws an exception of type E
     */
    default <T, E extends Throwable> T throwingTransaction(@NotNull ScopedValue.CallableOp<T, E> action) throws E {
        return ScopedValue.where(CURRENT_DATABASE, this).call(action);
    }

    /**
     * Creates a new {@link Database} instance based on the given {@link DataSource}.
     *
     * @param dataSource The data source to use for the database connection
     * @return A new {@link Database} instance or {@code null} if the database type is not supported
     * @throws SQLException If a database access error occurs
     */
    static @Nullable Database create(@NotNull DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String url = connection.getMetaData().getURL();
            if (url.startsWith("jdbc:sqlite:")) {
                return new SQLiteDatabase(dataSource);
            } else if (url.startsWith("jdbc:h2:")) {
                return new H2Database(dataSource);
            } else if (url.startsWith("jdbc:postgresql:")) {
                return new PostgreSQLDatabase(dataSource);
            } else {
                return null;
            }
        }
    }
}
