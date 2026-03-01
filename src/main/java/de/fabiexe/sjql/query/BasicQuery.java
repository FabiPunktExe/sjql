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

public abstract class BasicQuery<T, U> implements Query<U> {
    protected final Table<T> table;
    protected final ThrowingSupplier<Connection, SQLException> connectionSupplier;
    protected Expression condition = null;
    protected List<Map.Entry<Expression, Boolean>> ordering = new ArrayList<>();
    protected Long limit = null;

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
