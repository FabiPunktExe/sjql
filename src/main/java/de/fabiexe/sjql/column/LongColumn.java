package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import org.jetbrains.annotations.NotNull;

public final class LongColumn extends BasicColumn<Long> {
    public LongColumn(@NotNull Table<?> table, @NotNull String name) {
        super(table, name, Long.class);
    }
}
