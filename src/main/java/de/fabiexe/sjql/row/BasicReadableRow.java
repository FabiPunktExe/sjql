package de.fabiexe.sjql.row;

import de.fabiexe.sjql.Table;
import de.fabiexe.sjql.column.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class BasicReadableRow<T> implements ReadableRow<T> {
    private final Table<T> table;
    private final Map<Column<?>, Object> values;

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
            throw new IllegalArgumentException("Column " + column.name() + " does not exist in table " + table.name());
        }
    }

    @Override
    public boolean contains(@NotNull Column<?> column) {
        return table.hasColumn(column) && values.containsKey(column);
    }
}
