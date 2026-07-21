package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import de.fabiexe.sjql.expression.Expression;
import de.fabiexe.sjql.expression.dynamic.ColumnExpression;
import de.fabiexe.sjql.expression.logical.IsNotNullExpression;
import de.fabiexe.sjql.expression.logical.IsNullExpression;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Represents a column in a {@link Table}.
 *
 * @param <T> the type of the column
 */
public sealed interface Column<T extends @Nullable Object> permits PrimitiveColumn, ComplexColumn {
    /**
     * Gets the table this column belongs to.
     *
     * @return The table
     */
    Table<?> table();

    /**
     * Gets the name of the column.
     *
     * @return The name
     */
    String name();

    /**
     * Gets the type of the column.
     *
     * @return The type
     */
    Class<T> type();

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
    Column<T> defaultValue(Expression defaultValue);

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
    Column<@NonNull T> primaryKey();

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
    Column<@NonNull T> notNull();

    /**
     * Sets the default value of the column to a constant value.
     *
     * @param defaultValue the default value to set
     * @return this column for chaining
     */
    default Column<T> defaultValue(T defaultValue) {
        return defaultValue(Expression.constant(defaultValue));
    }

    /**
     * Creates an {@code =} comparison between this column and another expression.
     *
     * @param value the expression to compare with
     * @return a new {@link Expression}
     */
    default Expression eq(Expression value) {
        return new ColumnExpression<>(this).eq(value);
    }

    /**
     * Creates an {@code =} comparison between this column and another column.
     *
     * @param other the column to compare with
     * @return a new {@link Expression}
     */
    default Expression eq(Column<? extends T> other) {
        return eq(new ColumnExpression<>(other));
    }

    /**
     * Creates an {@code =} comparison between this column and a constant value.
     *
     * @param value the value to compare with
     * @return a new {@link Expression}
     */
    default Expression eq(T value) {
        return eq(Expression.constant(value));
    }

    /**
     * Creates a {@code !=} comparison between this column and another expression.
     *
     * @param value the expression to compare with
     * @return a new {@link Expression}
     */
    default Expression neq(Expression value) {
        return new ColumnExpression<>(this).neq(value);
    }

    /**
     * Creates a {@code !=} comparison between this column and another column.
     *
     * @param other the column to compare with
     * @return a new {@link Expression}
     */
    default Expression neq(Column<? extends T> other) {
        return neq(new ColumnExpression<>(other));
    }

    /**
     * Creates a {@code !=} comparison between this column and a constant value.
     *
     * @param value the value to compare with
     * @return a new {@link Expression}
     */
    default Expression neq(T value) {
        return neq(Expression.constant(value));
    }

    /**
     * Creates a {@code >} comparison between this column and another expression.
     *
     * @param value the expression to compare with
     * @return a new {@link Expression}
     */
    default Expression gt(Expression value) {
        return new ColumnExpression<>(this).gt(value);
    }

    /**
     * Creates a {@code >} comparison between this column and another column.
     *
     * @param other the column to compare with
     * @return a new {@link Expression}
     */
    default Expression gt(Column<? extends T> other) {
        return gt(new ColumnExpression<>(other));
    }

    /**
     * Creates a {@code >} comparison between this column and a constant value.
     *
     * @param value the value to compare with
     * @return a new {@link Expression}
     */
    default Expression gt(T value) {
        return gt(Expression.constant(value));
    }

    /**
     * Creates a {@code >=} comparison between this column and another expression.
     *
     * @param value the expression to compare with
     * @return a new {@link Expression}
     */
    default Expression gte(Expression value) {
        return new ColumnExpression<>(this).gte(value);
    }

    /**
     * Creates a {@code >=} comparison between this column and another column.
     *
     * @param other the column to compare with
     * @return a new {@link Expression}
     */
    default Expression gte(Column<? extends T> other) {
        return gte(new ColumnExpression<>(other));
    }

    /**
     * Creates a {@code >=} comparison between this column and a constant value.
     *
     * @param value the value to compare with
     * @return a new {@link Expression}
     */
    default Expression gte(T value) {
        return gte(Expression.constant(value));
    }

    /**
     * Creates a {@code <} comparison between this column and another expression.
     *
     * @param value the expression to compare with
     * @return a new {@link Expression}
     */
    default Expression lt(Expression value) {
        return new ColumnExpression<>(this).lt(value);
    }

    /**
     * Creates a {@code <} comparison between this column and another column.
     *
     * @param other the column to compare with
     * @return a new {@link Expression}
     */
    default Expression lt(Column<? extends T> other) {
        return lt(new ColumnExpression<>(other));
    }

    /**
     * Creates a {@code <} comparison between this column and a constant value.
     *
     * @param value the value to compare with
     * @return a new {@link Expression}
     */
    default Expression lt(T value) {
        return lt(Expression.constant(value));
    }

    /**
     * Creates a {@code <=} comparison between this column and another expression.
     *
     * @param value the expression to compare with
     * @return a new {@link Expression}
     */
    default Expression lte(Expression value) {
        return new ColumnExpression<>(this).lte(value);
    }

    /**
     * Creates a {@code <=} comparison between this column and another column.
     *
     * @param other the column to compare with
     * @return a new {@link Expression}
     */
    default Expression lte(Column<? extends T> other) {
        return lte(new ColumnExpression<>(other));
    }

    /**
     * Creates a {@code <=} comparison between this column and a constant value.
     *
     * @param value the value to compare with
     * @return a new {@link Expression}
     */
    default Expression lte(T value) {
        return lte(Expression.constant(value));
    }

    /**
     * Creates an {@code IS NULL} check for this column.
     *
     * @return a new {@link Expression}
     */
    default Expression checkNull() {
        return new IsNullExpression(new ColumnExpression<>(this));
    }

    /**
     * Creates an {@code IS NOT NULL} check for this column.
     *
     * @return a new {@link Expression}
     */
    default Expression checkNotNull() {
        return new IsNotNullExpression(new ColumnExpression<>(this));
    }
}