package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import de.fabiexe.sjql.expression.Expression;
import de.fabiexe.sjql.expression.dynamic.ColumnExpression;
import de.fabiexe.sjql.expression.logical.IsNotNullExpression;
import de.fabiexe.sjql.expression.logical.IsNullExpression;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a column in a {@link Table}.
 *
 * @param <T> the type of the column
 */
public sealed interface Column<T> permits PrimitiveColumn, ComplexColumn {
    /**
     * Gets the table this column belongs to.
     *
     * @return The table
     */
    @NotNull Table<?> table();

    /**
     * Gets the name of the column.
     *
     * @return The name
     */
    @NotNull String name();

    /**
     * Gets the type of the column.
     *
     * @return The type
     */
    @NotNull Class<T> type();

    /**
     * Gets the default value of the column
     *
     * @return The default value, or {@code null} if none is set
     */
    @Nullable Expression defaultValue();

    /**
     * Sets the default value of the column.
     *
     * @param defaultValue The default value to set
     * @return This column for chaining
     */
    @Contract("_ -> this")
    @NotNull Column<T> defaultValue(@NotNull Expression defaultValue);

    /**
     * Checks if the column is a primary key.
     *
     * @return {@code true} if the column is a primary key, otherwise {@code false}
     */
    boolean isPrimaryKey();

    /**
     * Sets this column as a primary key.
     *
     * @return This column for chaining
     */
    @Contract("-> this")
    @NotNull Column<@NotNull T> primaryKey();

    /**
     * Checks if the column has a not-null constraint.
     *
     * @return {@code true} if the column has a not-null constraint, otherwise {@code false}
     */
    boolean isNotNull();

    /**
     * Sets a not-null constraint on this column.
     *
     * @return This column for chaining
     */
    @Contract("-> this")
    @NotNull Column<@NotNull T> notNull();

    @Contract("_ -> this")
    default @NotNull Column<T> defaultValue(@NotNull T defaultValue) {
        return defaultValue(Expression.constant(defaultValue));
    }

    /**
     * Creates an {@code =} comparison between this column and another expression.
     *
     * @param value the expression to compare with
     * @return a new {@link Expression}
     */
    default @NotNull Expression eq(@NotNull Expression value) {
        return new ColumnExpression<>(this).eq(value);
    }

    /**
     * Creates an {@code =} comparison between this column and another column.
     *
     * @param other the column to compare with
     * @return a new {@link Expression}
     */
    default @NotNull Expression eq(@NotNull Column<? extends T> other) {
        return eq(new ColumnExpression<>(other));
    }

    /**
     * Creates an {@code =} comparison between this column and a constant value.
     *
     * @param value the value to compare with
     * @return a new {@link Expression}
     */
    default @NotNull Expression eq(@NotNull T value) {
        return eq(Expression.constant(value));
    }

    /**
     * Creates a {@code !=} comparison between this column and another expression.
     *
     * @param value the expression to compare with
     * @return a new {@link Expression}
     */
    default @NotNull Expression neq(@NotNull Expression value) {
        return new ColumnExpression<>(this).neq(value);
    }

    /**
     * Creates a {@code !=} comparison between this column and another column.
     *
     * @param other the column to compare with
     * @return a new {@link Expression}
     */
    default @NotNull Expression neq(@NotNull Column<? extends T> other) {
        return neq(new ColumnExpression<>(other));
    }

    /**
     * Creates a {@code !=} comparison between this column and a constant value.
     *
     * @param value the value to compare with
     * @return a new {@link Expression}
     */
    default @NotNull Expression neq(@NotNull T value) {
        return neq(Expression.constant(value));
    }

    /**
     * Creates a {@code >} comparison between this column and another expression.
     *
     * @param value the expression to compare with
     * @return a new {@link Expression}
     */
    default @NotNull Expression gt(@NotNull Expression value) {
        return new ColumnExpression<>(this).gt(value);
    }

    /**
     * Creates a {@code >} comparison between this column and another column.
     *
     * @param other the column to compare with
     * @return a new {@link Expression}
     */
    default @NotNull Expression gt(@NotNull Column<? extends T> other) {
        return gt(new ColumnExpression<>(other));
    }

    /**
     * Creates a {@code >} comparison between this column and a constant value.
     *
     * @param value the value to compare with
     * @return a new {@link Expression}
     */
    default @NotNull Expression gt(@NotNull T value) {
        return gt(Expression.constant(value));
    }

    /**
     * Creates a {@code >=} comparison between this column and another expression.
     *
     * @param value the expression to compare with
     * @return a new {@link Expression}
     */
    default @NotNull Expression gte(@NotNull Expression value) {
        return new ColumnExpression<>(this).gte(value);
    }

    /**
     * Creates a {@code >=} comparison between this column and another column.
     *
     * @param other the column to compare with
     * @return a new {@link Expression}
     */
    default @NotNull Expression gte(@NotNull Column<? extends T> other) {
        return gte(new ColumnExpression<>(other));
    }

    /**
     * Creates a {@code >=} comparison between this column and a constant value.
     *
     * @param value the value to compare with
     * @return a new {@link Expression}
     */
    default @NotNull Expression gte(@NotNull T value) {
        return gte(Expression.constant(value));
    }

    /**
     * Creates a {@code <} comparison between this column and another expression.
     *
     * @param value the expression to compare with
     * @return a new {@link Expression}
     */
    default @NotNull Expression lt(@NotNull Expression value) {
        return new ColumnExpression<>(this).lt(value);
    }

    /**
     * Creates a {@code <} comparison between this column and another column.
     *
     * @param other the column to compare with
     * @return a new {@link Expression}
     */
    default @NotNull Expression lt(@NotNull Column<? extends T> other) {
        return lt(new ColumnExpression<>(other));
    }

    /**
     * Creates a {@code <} comparison between this column and a constant value.
     *
     * @param value the value to compare with
     * @return a new {@link Expression}
     */
    default @NotNull Expression lt(@NotNull T value) {
        return lt(Expression.constant(value));
    }

    /**
     * Creates a {@code <=} comparison between this column and another expression.
     *
     * @param value the expression to compare with
     * @return a new {@link Expression}
     */
    default @NotNull Expression lte(@NotNull Expression value) {
        return new ColumnExpression<>(this).lte(value);
    }

    /**
     * Creates a {@code <=} comparison between this column and another column.
     *
     * @param other the column to compare with
     * @return a new {@link Expression}
     */
    default @NotNull Expression lte(@NotNull Column<? extends T> other) {
        return lte(new ColumnExpression<>(other));
    }

    /**
     * Creates a {@code <=} comparison between this column and a constant value.
     *
     * @param value the value to compare with
     * @return a new {@link Expression}
     */
    default @NotNull Expression lte(@NotNull T value) {
        return lte(Expression.constant(value));
    }

    /**
     * Creates an {@code IS NULL} check for this column.
     *
     * @return a new {@link Expression}
     */
    default @NotNull Expression checkNull() {
        return new IsNullExpression(new ColumnExpression<>(this));
    }

    /**
     * Creates an {@code IS NOT NULL} check for this column.
     *
     * @return a new {@link Expression}
     */
    default @NotNull Expression checkNotNull() {
        return new IsNotNullExpression(new ColumnExpression<>(this));
    }
}