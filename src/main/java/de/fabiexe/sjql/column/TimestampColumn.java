package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import org.jspecify.annotations.Nullable;

import java.time.Instant;

/** A timestamp column. */
public final class TimestampColumn extends PrimitiveColumn<@Nullable Instant> {
    /**
     * Creates a new timestamp column.
     *
     * @param table the table this column belongs to
     * @param name the column name
     */
    public TimestampColumn(Table<?> table, String name) {
        super(table, name, Instant.class);
    }
}