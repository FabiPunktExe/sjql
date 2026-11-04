package de.fabiexe.sjql.query;

import de.fabiexe.sjql.Query;
import de.fabiexe.sjql.expression.Expression;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * A query that always returns a pre-defined constant value.
 *
 * @param <T> the type of the constant value
 * @param value the constant value which should returned by {@link #execute()}
 */
public record ConstantQuery<T extends @Nullable Object>(T value) implements Query<T> {
    @Override
    public Query<T> where(@Nullable Expression condition) {
        return this;
    }

    @Override
    public Query<T> orderBy(Expression expression, boolean ascending) {
        return this;
    }

    @Override
    public Query<T> orderBy(List<Map.Entry<Expression, Boolean>> ordering) {
        return this;
    }

    @Override
    public Query<T> limit(@Nullable Long limit) {
        return this;
    }

    @Override
    public Query<Long> count() {
        return new ConstantQuery<>(1L);
    }

    @Override
    public @Nullable T execute() {
        return value;
    }
}