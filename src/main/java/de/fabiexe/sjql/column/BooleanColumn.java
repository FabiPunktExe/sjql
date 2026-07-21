package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import org.jspecify.annotations.Nullable;

/** A primitive boolean column */
public final class BooleanColumn extends PrimitiveColumn<@Nullable Boolean> {
    /**
     * Creates a new boolean column.
     *
     * @param table the table this column belongs to
     * @param name the column name
     */
    public BooleanColumn(Table<?> table, String name) {
        super(table, name, Boolean.class);
    }
}