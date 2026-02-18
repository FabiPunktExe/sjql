package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import org.jetbrains.annotations.NotNull;

public final class StringColumn extends BasicColumn<String> {
    private final int maxLength;

    public StringColumn(@NotNull Table<?> table, @NotNull String name, int maxLength) {
        super(table, name, String.class);
        if (maxLength <= 0) {
            throw new IllegalArgumentException("maxLength must be positive");
        }
        this.maxLength = maxLength;
    }

    public int getMaxLength() {
        return maxLength;
    }
}
