package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/** A UUID column. */
public final class UUIDColumn extends PrimitiveColumn<UUID> {
    /**
     * Creates a new UUID column.
     *
     * @param table the table this column belongs to
     * @param name the column name
     */
    public UUIDColumn(@NotNull Table<?> table, @NotNull String name) {
        super(table, name, UUID.class);
    }
}
