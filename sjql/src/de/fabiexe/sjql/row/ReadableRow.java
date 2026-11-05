package de.fabiexe.sjql.row;

import de.fabiexe.sjql.column.Column;
import de.fabiexe.sjql.column.ColumnRef;
import org.jspecify.annotations.Nullable;

/** A read-only view of a database row. */
public interface ReadableRow {
    /**
     * Gets the value of the given column from this row.
     *
     * @param <T> the type of the column
     * @param column the column to read
     * @return the column value, or {@code null} if the column value is SQL {@code NULL}
     */
    <T extends @Nullable Object> T get(Column<T> column);

    /**
     * Gets the value of the given column from this row.
     *
     * @param <T> The type of the column
     * @param column The column reference to read
     * @return The column value, or {@code null} if the column value is SQL {@code NULL}
     * @throws UnsupportedOperationException If this row implementation does not support qualified columns
     */
    <T extends @Nullable Object> T get(ColumnRef<T> column);

    /**
     * Checks whether this row contains a value for the given column.
     *
     * @param column The column to check
     * @return {@code true} if the column has a value in this row
     */
    boolean contains(Column<?> column);

    /**
     * Checks whether this row contains a value for the given column.
     *
     * @param column The column reference to check
     * @return {@code true} if the column has a value in this row
     */
    boolean contains(ColumnRef<?> column);
}