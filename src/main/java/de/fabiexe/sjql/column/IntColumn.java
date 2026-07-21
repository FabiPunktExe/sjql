package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import org.jspecify.annotations.Nullable;

/** A primitive 32-bit integer column. */
public final class IntColumn extends PrimitiveColumn<@Nullable Integer> {
    /**
     * Creates a new integer column.
     *
     * @param table the table this column belongs to
     * @param name the column name
     */
    public IntColumn(Table<?> table, String name) {
        super(table, name, Integer.class);
    }
}