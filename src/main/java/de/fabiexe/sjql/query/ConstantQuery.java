package de.fabiexe.sjql.query;

import de.fabiexe.sjql.Query;
import de.fabiexe.sjql.expression.Expression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public record ConstantQuery<T>(@Nullable T value) implements Query<T> {
    @Override
    public @NotNull Query<T> where(@NotNull Expression condition) {
        return this;
    }

    @Override
    public @NotNull Query<T> orderBy(@NotNull Expression expression, boolean ascending) {
        return this;
    }

    @Override
    public @NotNull Query<T> orderBy(@NotNull List<Map.Entry<Expression, Boolean>> ordering) {
        return this;
    }

    @Override
    public @NotNull Query<T> limit(@Nullable Long limit) {
        return this;
    }

    @Override
    public @NotNull Query<Long> count() {
        return new ConstantQuery<>(1L);
    }

    @Override
    public @Nullable T execute() {
        return value;
    }
}
