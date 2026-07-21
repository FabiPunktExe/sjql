package de.fabiexe.sjql.row;

import de.fabiexe.sjql.Table;
import de.fabiexe.sjql.column.Column;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of {@link ReadableRow} backed by a map of column values.
 *
 * @param <T> the type of the objects that represent rows in the table
 */
public class BasicReadableRow<T extends @Nullable Object> implements ReadableRow {
    private final Table<T> table;
    private final Map<Column<?>, @Nullable Object> values;

    /**
     * Creates a new readable row for the given table and column values.
     *
     * @param table the table this row belongs to
     * @param values the column values
     */
    public BasicReadableRow(Table<T> table, Map<Column<?>, @Nullable Object> values) {
        this.table = table;
        this.values = new HashMap<>(values);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U extends @Nullable Object> U get(Column<U> column) {
        if (table.hasColumn(column)) {
            return (U) values.get(column);
        } else {
            throw new IllegalArgumentException("Column " + column.name() + " does not exist in table " + table.getName());
        }
    }

    @Override
    public boolean contains(Column<?> column) {
        return table.hasColumn(column) && values.containsKey(column);
    }
}