package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public final class TimestampColumn extends BasicColumn<Instant> {
    public TimestampColumn(@NotNull Table<?> table, @NotNull String name) {
        super(table, name, Instant.class);
    }
}
