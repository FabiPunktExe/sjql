package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

/** A UUID column. */
public final class UUIDColumn extends PrimitiveColumn<@Nullable UUID> {
    /**
     * Creates a new UUID column.
     *
     * @param table the table this column belongs to
     * @param name the column name
     */
    public UUIDColumn(Table<?> table, String name) {
        super(table, name, UUID.class);
    }
}