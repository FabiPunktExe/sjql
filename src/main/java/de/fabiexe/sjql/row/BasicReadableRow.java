package de.fabiexe.sjql.row;

import de.fabiexe.sjql.Table;
import de.fabiexe.sjql.column.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of {@link ReadableRow} backed by a map of column values.
 *
 * @param <T> the type of the objects that represent rows in the table
 */
public class BasicReadableRow<T> implements ReadableRow<T> {
    private final Table<T> table;
    private final Map<Column<?>, Object> values;

    /**
     * Creates a new readable row for the given table and column values.
     *
     * @param table the table this row belongs to
     * @param values the column values
     */
    public BasicReadableRow(@NotNull Table<T> table, @NotNull Map<Column<?>, Object> values) {
        this.table = table;
        this.values = new HashMap<>(values);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> @Nullable U get(@NotNull Column<U> column) {
        if (table.hasColumn(column)) {
            return (U) values.get(column);
        } else {
            throw new IllegalArgumentException("Column " + column.name() + " does not exist in table " + table.getName());
        }
    }

    @Override
    public boolean contains(@NotNull Column<?> column) {
        return table.hasColumn(column) && values.containsKey(column);
    }
}