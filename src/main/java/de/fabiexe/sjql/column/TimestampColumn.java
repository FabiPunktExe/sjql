package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/** A timestamp column. */
public final class TimestampColumn extends PrimitiveColumn<Instant> {
    /**
     * Creates a new timestamp column.
     *
     * @param table the table this column belongs to
     * @param name the column name
     */
    public TimestampColumn(@NotNull Table<?> table, @NotNull String name) {
        super(table, name, Instant.class);
    }
}
