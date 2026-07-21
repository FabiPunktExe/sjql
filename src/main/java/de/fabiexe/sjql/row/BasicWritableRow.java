package de.fabiexe.sjql.row;

import de.fabiexe.sjql.Table;
import de.fabiexe.sjql.column.Column;
import de.fabiexe.sjql.expression.Expression;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of {@link WritableRow} backed by a map of column expressions.
 *
 * @param <T> the type of the objects that represent rows in the table
 */
public class BasicWritableRow<T extends @Nullable Object> implements WritableRow {
    private final Table<T> table;
    private final Map<Column<?>, Expression> values = new HashMap<>();

    /**
     * Creates a new writable row for the given table.
     *
     * @param table the table this row belongs to
     */
    public BasicWritableRow(Table<T> table) {
        this.table = table;
    }

    @Override
    public <U extends @Nullable Object> void set(Column<U> column, Expression value) {
        if (table.hasColumn(column)) {
            values.put(column, value);
        } else {
            throw new IllegalArgumentException("Column " + column.name() + " does not exist in table " + table.getName());
        }
    }

    @Override
    public <U extends @Nullable Object> @Nullable Expression get(Column<U> column) {
        if (table.hasColumn(column)) {
            return values.getOrDefault(column, column.defaultValue());
        } else {
            throw new IllegalArgumentException("Column " + column.name() + " does not exist in table " + table.getName());
        }
    }

    @Override
    public boolean contains(Column<?> column) {
        return table.hasColumn(column) && values.containsKey(column);
    }
}