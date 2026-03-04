package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class UUIDColumn extends PrimitiveColumn<UUID> {
    public UUIDColumn(@NotNull Table<?> table, @NotNull String name) {
        super(table, name, UUID.class);
    }
}
