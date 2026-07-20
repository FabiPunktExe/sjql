package de.fabiexe.sjql.query;

import de.fabiexe.sjql.Query;
import de.fabiexe.sjql.Table;
import de.fabiexe.sjql.expression.Expression;
import de.fabiexe.sjql.util.ThrowingSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Base implementation of {@link Query} that stores the table, connection supplier and query modifiers.
 *
 * @param <T> the type of the objects that represent rows in the queried table
 * @param <U> the type of the query result
 */
public abstract class BasicQuery<T, U> implements Query<U> {
    /** The table this query operates on. */
    protected final Table<T> table;

    /** Supplier for the database connection used to execute the query. */
    protected final ThrowingSupplier<Connection, SQLException> connectionSupplier;

    /** The optional {@code WHERE} condition, or {@code null} if none was set. */
    protected Expression condition = null;

    /** The {@code ORDER BY} expressions together with their sort direction. */
    protected List<Map.Entry<Expression, Boolean>> ordering = new ArrayList<>();

    /** The optional {@code LIMIT}, or {@code null} if none was set. */
    protected Long limit = null;

    /**
     * Creates a new basic query for the given table.
     *
     * @param table the table to query
     * @param connectionSupplier supplier for the database connection
     */
    public BasicQuery(@NotNull Table<T> table, @NotNull ThrowingSupplier<Connection, SQLException> connectionSupplier) {
        this.table = table;
        this.connectionSupplier = connectionSupplier;
    }

    @Override
    public @NotNull Query<U> where(@NotNull Expression condition) {
        this.condition = condition;
        return this;
    }

    @Override
    public @NotNull Query<U> orderBy(@NotNull Expression expression, boolean ascending) {
        this.ordering.add(Map.entry(expression, ascending));
        return this;
    }

    @Override
    public @NotNull Query<U> orderBy(@NotNull List<Map.Entry<Expression, Boolean>> ordering) {
        this.ordering.addAll(ordering);
        return this;
    }

    @Override
    public @NotNull Query<U> limit(@Nullable Long limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public @NotNull Query<Long> count() {
        return new BasicCountQuery<>(this);
    }
}