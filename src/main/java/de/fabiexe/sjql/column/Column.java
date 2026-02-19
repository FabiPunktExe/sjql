package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import de.fabiexe.sjql.expression.Expression;
import de.fabiexe.sjql.expression.dynamic.ColumnExpression;
import de.fabiexe.sjql.expression.logical.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public sealed interface Column<T> permits BasicColumn {
    @NotNull Table<?> table();
    @NotNull String name();
    @NotNull Class<T> type();
    @Nullable Expression defaultValue();
    @Contract("_ -> this")
    @NotNull Column<T> defaultValue(@NotNull Expression defaultValue);

    @Contract("_ -> this")
    default @NotNull Column<T> defaultValue(@NotNull T defaultValue) {
        return defaultValue(Expression.constant(defaultValue));
    }

    default @NotNull Expression eq(@NotNull Expression value) {
        return new EqualsExpression(new ColumnExpression<>(this), value);
    }

    default @NotNull Expression eq(@NotNull Column<? extends T> other) {
        return eq(new ColumnExpression<>(other));
    }

    default @NotNull Expression eq(@NotNull T value) {
        return eq(Expression.constant(value));
    }

    default @NotNull Expression ne(@NotNull Expression value) {
        return new NotEqualsExpression(new ColumnExpression<>(this), value);
    }

    default @NotNull Expression ne(@NotNull Column<? extends T> other) {
        return ne(new ColumnExpression<>(other));
    }

    default @NotNull Expression ne(@NotNull T value) {
        return ne(Expression.constant(value));
    }

    default @NotNull Expression gt(@NotNull Expression value) {
        return new GreaterThanExpression(new ColumnExpression<>(this), value);
    }

    default @NotNull Expression gt(@NotNull Column<? extends T> other) {
        return gt(new ColumnExpression<>(other));
    }

    default @NotNull Expression gt(@NotNull T value) {
        return gt(Expression.constant(value));
    }

    default @NotNull Expression gte(@NotNull Expression value) {
        return new GreaterThanOrEqualExpression(new ColumnExpression<>(this), value);
    }

    default @NotNull Expression gte(@NotNull Column<? extends T> other) {
        return gte(new ColumnExpression<>(other));
    }

    default @NotNull Expression gte(@NotNull T value) {
        return gte(Expression.constant(value));
    }

    default @NotNull Expression lt(@NotNull Expression value) {
        return new LessThanExpression(new ColumnExpression<>(this), value);
    }

    default @NotNull Expression lt(@NotNull Column<? extends T> other) {
        return lt(new ColumnExpression<>(other));
    }

    default @NotNull Expression lt(@NotNull T value) {
        return lt(Expression.constant(value));
    }

    default @NotNull Expression lte(@NotNull Expression value) {
        return new LessThanOrEqualExpression(new ColumnExpression<>(this), value);
    }

    default @NotNull Expression lte(@NotNull Column<? extends T> other) {
        return lte(new ColumnExpression<>(other));
    }

    default @NotNull Expression lte(@NotNull T value) {
        return lte(Expression.constant(value));
    }
}
