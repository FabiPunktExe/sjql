package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import org.jetbrains.annotations.NotNull;

/** A primitive 32-bit integer column. */
public final class IntColumn extends PrimitiveColumn<Integer> {
    /**
     * Creates a new integer column.
     *
     * @param table the table this column belongs to
     * @param name the column name
     */
    public IntColumn(@NotNull Table<?> table, @NotNull String name) {
        super(table, name, Integer.class);
    }
}
