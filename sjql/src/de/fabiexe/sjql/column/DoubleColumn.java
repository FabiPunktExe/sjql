package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import org.jspecify.annotations.Nullable;

/** A primitive 64-bit floating point column. */
public final class DoubleColumn extends PrimitiveColumn<@Nullable Double> {
    /**
     * Creates a new double column.
     *
     * @param table the table this column belongs to
     * @param name the column name
     */
    public DoubleColumn(Table<?> table, String name) {
        super(table, name, Double.class);
    }
}