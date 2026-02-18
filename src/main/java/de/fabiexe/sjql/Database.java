package de.fabiexe.sjql;

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
import java.util.function.Supplier;

public interface Database {
    void createTable(@NotNull Table<?> table) throws SQLException;
    void deleteTable(@NotNull Table<?> table) throws SQLException;
    boolean tableExists(@NotNull Table<?> table) throws SQLException;
    <T> void insert(@NotNull Table<T> table, @NotNull WritableRow row) throws SQLException;
    <T> @NotNull Query<List<ReadableRow<T>>> selectRows(@NotNull Table<T> table);
    <T> @NotNull Query<List<T>> select(@NotNull Table<T> table);

    default @NotNull Query<Long> count(@NotNull Table<?> table) {
        return selectRows(table).count();
    }

    default void transaction(@NotNull Runnable action) {
        ScopedValue.where(CURRENT_DATABASE, this).run(action);
    }

    default <T extends Throwable> void throwingTransaction(@NotNull ThrowingRunnable<T> action) throws T {
        ScopedValue.where(CURRENT_DATABASE, this).call(() -> {
            action.run();
            return null;
        });
    }

    default <T> T transaction(@NotNull Supplier<T> action) {
        return ScopedValue.where(CURRENT_DATABASE, this).call(action::get);
    }

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
            } else {
                return null;
            }
        }
    }

    ScopedValue<Database> CURRENT_DATABASE = ScopedValue.newInstance();
}
