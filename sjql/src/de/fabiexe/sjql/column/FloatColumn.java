package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import org.jspecify.annotations.Nullable;

/** A primitive 32-bit floating point column. */
public final class FloatColumn extends PrimitiveColumn<@Nullable Float> {
    /**
     * Creates a new float column.
     *
     * @param table the table this column belongs to
     * @param name the column name
     */
    public FloatColumn(Table<?> table, String name) {
        super(table, name, Float.class);
    }
}