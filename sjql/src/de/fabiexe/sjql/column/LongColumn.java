package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import org.jspecify.annotations.Nullable;

/** A primitive 64-bit integer column. */
public final class LongColumn extends PrimitiveColumn<@Nullable Long> {
    /**
     * Creates a new long column.
     *
     * @param table the table this column belongs to
     * @param name the column name
     */
    public LongColumn(Table<?> table, String name) {
        super(table, name, Long.class);
    }
}