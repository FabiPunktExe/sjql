package de.fabiexe.sjql.row;

import de.fabiexe.sjql.column.Column;
import de.fabiexe.sjql.expression.Expression;
import org.jspecify.annotations.Nullable;

/** A mutable row used to insert and update values. */
public interface WritableRow {
    /**
     * Sets the value of the given column to an expression.
     *
     * @param <U> the type of the column
     * @param column the column to set
     * @param value the expression to set
     */
    <U extends @Nullable Object> void set(Column<U> column, Expression value);

    /**
     * Gets the expression set for the given column, or the column's default value.
     *
     * @param <U> the type of the column
     * @param column the column to read
     * @return the expression for the column, or {@code null} if none is set and no default exists
     */
    <U extends @Nullable Object> @Nullable Expression get(Column<U> column);

    /**
     * Checks whether this row contains a value for the given column.
     *
     * @param column the column to check
     * @return {@code true} if a value has been set for the column
     */
    boolean contains(Column<?> column);

    /**
     * Sets the value of the given column to a constant value.
     *
     * @param <U> the type of the column
     * @param column the column to set
     * @param value the value to set, or {@code null}
     */
    default <U extends @Nullable Object> void set(Column<U> column, U value) {
        set(column, Expression.constant(value));
    }
}