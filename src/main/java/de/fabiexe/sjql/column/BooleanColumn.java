package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import org.jetbrains.annotations.NotNull;

public final class BooleanColumn extends PrimitiveColumn<Boolean> {
    public BooleanColumn(@NotNull Table<?> table, @NotNull String name) {
        super(table, name, Boolean.class);
    }
}
