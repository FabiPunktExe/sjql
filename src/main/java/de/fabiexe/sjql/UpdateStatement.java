package de.fabiexe.sjql;

import de.fabiexe.sjql.expression.Expression;
import org.jetbrains.annotations.NotNull;

/** Represents a SQL {@code UPDATE} statement that can be executed to update rows in a table. */
public interface UpdateStatement extends Statement {
    /**
     * Adds a {@code WHERE} clause to the {@code UPDATE} statement with the specified condition.
     *
     * @param condition The condition to be applied in the {@code WHERE} clause
     * @return An {@link UpdateStatement} with the added {@code WHERE} clause
     */
    @NotNull UpdateStatement where(@NotNull Expression condition);
}
