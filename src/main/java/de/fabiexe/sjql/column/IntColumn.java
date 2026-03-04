package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import org.jetbrains.annotations.NotNull;

public final class IntColumn extends PrimitiveColumn<Integer> {
    public IntColumn(@NotNull Table<?> table, @NotNull String name) {
        super(table, name, Integer.class);
    }
}
