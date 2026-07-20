package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import org.jetbrains.annotations.NotNull;

/** A primitive 32-bit floating point column. */
public final class FloatColumn extends PrimitiveColumn<Float> {
    /**
     * Creates a new float column.
     *
     * @param table the table this column belongs to
     * @param name the column name
     */
    public FloatColumn(@NotNull Table<?> table, @NotNull String name) {
        super(table, name, Float.class);
    }
}
