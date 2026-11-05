package de.fabiexe.sjql.column;

import de.fabiexe.sjql.TableRef;
import org.jspecify.annotations.Nullable;

/**
 * A reference to a {@link Column}
 *
 * @param <T> The type of the column
 * @param tableRef The table reference this column belongs to
 * @param column The referenced column
 */
public record ColumnRefImpl<T extends @Nullable Object>(
        TableRef<?> tableRef,
        Column<T> column
) implements ColumnRef<T> {
    @Override
    public TableRef<?> getTableRef() {
        return tableRef;
    }

    @Override
    public String name() {
        return column.name();
    }
}