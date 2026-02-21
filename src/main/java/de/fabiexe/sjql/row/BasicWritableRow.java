package de.fabiexe.sjql.row;

import de.fabiexe.sjql.Table;
import de.fabiexe.sjql.column.Column;
import de.fabiexe.sjql.expression.Expression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class BasicWritableRow<T> implements WritableRow {
    private final Table<T> table;
    private final Map<Column<?>, Expression> values = new HashMap<>();

    public BasicWritableRow(@NotNull Table<T> table) {
        this.table = table;
    }

    @Override
    public <U> void set(@NotNull Column<U> column, @NotNull Expression value) {
        if (table.hasColumn(column)) {
            values.put(column, value);
        } else {
            throw new IllegalArgumentException("Column " + column.name() + " does not exist in table " + table.getName());
        }
    }

    @Override
    public <U> @Nullable Expression get(@NotNull Column<U> column) {
        if (table.hasColumn(column)) {
            return values.getOrDefault(column, column.defaultValue());
        } else {
            throw new IllegalArgumentException("Column " + column.name() + " does not exist in table " + table.getName());
        }
    }

    @Override
    public boolean contains(@NotNull Column<?> column) {
        return table.hasColumn(column) && values.containsKey(column);
    }
}
