package de.fabiexe.sjql.row;

import de.fabiexe.sjql.column.Column;
import org.jspecify.annotations.Nullable;

/** A read-only view of a database row. */
public interface ReadableRow {
    /**
     * Gets the value of the given column from this row.
     *
     * @param <U> the type of the column
     * @param column the column to read
     * @return the column value, or {@code null} if the column value is SQL {@code NULL}
     */
    <U extends @Nullable Object> U get(Column<U> column);

    /**
     * Checks whether this row contains a value for the given column.
     *
     * @param column the column to check
     * @return {@code true} if the column has a value in this row
     */
    boolean contains(Column<?> column);
}