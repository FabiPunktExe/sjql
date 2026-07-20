package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import org.jetbrains.annotations.NotNull;

/** A variable-length character column. */
public final class StringColumn extends PrimitiveColumn<String> {
    private final int maxLength;

    /**
     * Creates a new string column with the given maximum length.
     *
     * @param table the table this column belongs to
     * @param name the column name
     * @param maxLength the maximum length of the string
     * @throws IllegalArgumentException if {@code maxLength} is not positive
     */
    public StringColumn(@NotNull Table<?> table, @NotNull String name, int maxLength) {
        super(table, name, String.class);
        if (maxLength <= 0) {
            throw new IllegalArgumentException("maxLength must be positive");
        }
        this.maxLength = maxLength;
    }

    /**
     * Gets the maximum length of this string column.
     *
     * @return the maximum length
     */
    public int getMaxLength() {
        return maxLength;
    }
}