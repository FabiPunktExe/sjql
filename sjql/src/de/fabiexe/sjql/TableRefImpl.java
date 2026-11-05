package de.fabiexe.sjql;

import de.fabiexe.sjql.column.Column;
import de.fabiexe.sjql.column.ColumnRef;
import de.fabiexe.sjql.column.ColumnRefImpl;
import de.fabiexe.sjql.row.ReadableRow;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * The default implementation of {@link TableRef}.
 *
 * @param <T> The type of the objects that represent rows in the referenced table
 * @param table The referenced table
 * @param alias The alias of the reference, or {@code null} for no alias
 */
public record TableRefImpl<T extends @Nullable Object>(Table<T> table, @Nullable String alias) implements TableRef<T> {
    @Override
    public Table<T> getTable() {
        return table;
    }

    @Override
    public @Nullable String getAlias() {
        return alias;
    }

    @Override
    public String getQualifier() {
        return alias != null ? alias : table.getName();
    }

    @Override
    public <U extends @Nullable Object> ColumnRef<U> column(Column<U> column) {
        if (table.hasColumn(column)) {
            return new ColumnRefImpl<>(this, column);
        } else {
            throw new IllegalArgumentException("Column " + column.name() + " does not exist in table " + table.getName());
        }
    }

    @Override
    public Query<List<ReadableRow>> combine(de.fabiexe.sjql.TableRef<?>... others) {
        return Database.CURRENT_DATABASE
                .orElseThrow(() -> new IllegalStateException("This method can only be called inside a transaction"))
                .combine(this, others);
    }
}