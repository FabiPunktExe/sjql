package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import org.jetbrains.annotations.NotNull;

/** A primitive boolean column */
public final class BooleanColumn extends PrimitiveColumn<Boolean> {
    /**
     * Creates a new boolean column.
     *
     * @param table the table this column belongs to
     * @param name the column name
     */
    public BooleanColumn(@NotNull Table<?> table, @NotNull String name) {
        super(table, name, Boolean.class);
    }
}