package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import org.jetbrains.annotations.NotNull;

/** A primitive 64-bit integer column. */
public final class LongColumn extends PrimitiveColumn<Long> {
    /**
     * Creates a new long column.
     *
     * @param table the table this column belongs to
     * @param name the column name
     */
    public LongColumn(@NotNull Table<?> table, @NotNull String name) {
        super(table, name, Long.class);
    }
}
