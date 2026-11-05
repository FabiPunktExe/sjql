package de.fabiexe.sjql.row;

import de.fabiexe.sjql.TableRef;
import de.fabiexe.sjql.column.Column;
import de.fabiexe.sjql.column.ColumnRef;
import org.jspecify.annotations.Nullable;

import java.util.*;

/** Default implementation of {@link ReadableRow} backed by a map of column values. */
public class BasicReadableRow implements ReadableRow {
    private final List<TableRef<?>> tables;
    private final Map<ColumnRef<?>, @Nullable Object> values;

    /**
     * Creates a new readable row for the given table and column values.
     *
     * @param tables The tables participating in the row
     * @param values The column values
     */
    public BasicReadableRow(List<TableRef<?>> tables, Map<ColumnRef<?>, @Nullable Object> values) {
        this.tables = tables;
        this.values = new HashMap<>(values);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends @Nullable Object> T get(Column<T> column) {
        List<T> values = new ArrayList<>();
        for (TableRef<?> tableRef : tables) {
            if (tableRef.getTable().hasColumn(column)) {
                values.add((T) this.values.get(tableRef.column(column)));
            }
        }
        if (values.isEmpty()) {
            throw new IllegalArgumentException("Column " + column.name() + " does not exist in this row");
        } else if (values.size() == 1) {
            return values.getFirst();
        } else {
            throw new IllegalArgumentException("Column " + column.name() + " exists in multiple tables in this row. Use a column reference instead.");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends @Nullable Object> T get(ColumnRef<T> column) {
        for (TableRef<?> tableRef : tables) {
            if (tableRef.equals(column.getTableRef())) {
                return (T) values.get(column);
            }
        }
        throw new IllegalArgumentException("Column " + column.name() + " does not exist in this row");
    }

    @Override
    public boolean contains(Column<?> column) {
        for (TableRef<?> tableRef : tables) {
            if (tableRef.getTable().hasColumn(column)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(ColumnRef<?> column) {
        for (TableRef<?> tableRef : tables) {
            if (tableRef.equals(column.getTableRef())) {
                return true;
            }
        }
        return false;
    }
}