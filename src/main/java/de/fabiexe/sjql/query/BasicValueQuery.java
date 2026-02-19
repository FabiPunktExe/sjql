package de.fabiexe.sjql.query;

import de.fabiexe.sjql.Query;
import de.fabiexe.sjql.Table;
import de.fabiexe.sjql.row.ReadableRow;
import de.fabiexe.sjql.util.ThrowingSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BasicValueQuery<T> extends BasicQuery<T, List<T>> {
    public BasicValueQuery(
            @NotNull Table<T> table,
            @NotNull ThrowingSupplier<Connection, SQLException> connectionSupplier
    ) {
        super(table, connectionSupplier);
    }

    @Override
    public @Nullable List<T> execute() throws SQLException {
        List<T> result = new ArrayList<>();
        Query<List<ReadableRow<T>>> rowQuery = new BasicRowQuery<>(table, connectionSupplier)
                .where(condition)
                .orderBy(ordering)
                .limit(limit);
        for (ReadableRow<T> row : rowQuery.executeNotNull()) {
            result.add(table.getRowMapper().apply(row));
        }
        return result;
    }
}
