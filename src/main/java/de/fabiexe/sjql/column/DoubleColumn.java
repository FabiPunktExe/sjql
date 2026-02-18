package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import org.jetbrains.annotations.NotNull;

public final class DoubleColumn extends BasicColumn<Double> {
    public DoubleColumn(@NotNull Table<?> table, @NotNull String name) {
        super(table, name, Double.class);
    }
}
