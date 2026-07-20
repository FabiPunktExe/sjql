package de.fabiexe.sjql.row;

import de.fabiexe.sjql.column.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A read-only view of a database row.
 *
 * @param <T> the type of the objects that represent rows in the table
 */
public interface ReadableRow<T> {
    /**
     * Gets the value of the given column from this row.
     *
     * @param <U> the type of the column
     * @param column the column to read
     * @return the column value, or {@code null} if the column value is SQL {@code NULL}
     */
    <U> @Nullable U get(@NotNull Column<U> column);

    /**
     * Checks whether this row contains a value for the given column.
     *
     * @param column the column to check
     * @return {@code true} if the column has a value in this row
     */
    boolean contains(@NotNull Column<?> column);
}