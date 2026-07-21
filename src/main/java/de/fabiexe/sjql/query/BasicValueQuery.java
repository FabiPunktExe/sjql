package de.fabiexe.sjql.query;

import de.fabiexe.sjql.Query;
import de.fabiexe.sjql.Table;
import de.fabiexe.sjql.database.BasicDatabase;
import de.fabiexe.sjql.row.ReadableRow;
import de.fabiexe.sjql.util.ThrowingSupplier;
import org.jspecify.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A query that returns mapped objects of type {@code T}.
 *
 * @param <T> the type of the objects that represent rows in the queried table
 */
public class BasicValueQuery<T extends @Nullable Object> extends BasicQuery<T, List<T>> {
    /**
     * Creates a new value query for the given table.
     *
     * @param database the database to query
     * @param table the table to query
     * @param connectionSupplier supplier for the database connection
     */
    public BasicValueQuery(
            BasicDatabase database,
            Table<T> table,
            ThrowingSupplier<Connection, SQLException> connectionSupplier
    ) {
        super(database, table, connectionSupplier);
    }

    @Override
    public @Nullable List<T> execute() throws SQLException {
        List<T> result = new ArrayList<>();
        Query<List<ReadableRow>> rowQuery = new BasicRowQuery<>(database, table, connectionSupplier)
                .where(condition)
                .orderBy(ordering)
                .limit(limit);
        for (ReadableRow row : rowQuery.executeNotNull()) {
            result.add(table.getRowMapper().apply(row));
        }
        return result;
    }
}