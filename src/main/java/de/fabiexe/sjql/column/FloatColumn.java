package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import org.jetbrains.annotations.NotNull;

public final class FloatColumn extends PrimitiveColumn<Float> {
    public FloatColumn(@NotNull Table<?> table, @NotNull String name) {
        super(table, name, Float.class);
    }
}
