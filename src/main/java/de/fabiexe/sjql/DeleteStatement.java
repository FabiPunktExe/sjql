package de.fabiexe.sjql;

import de.fabiexe.sjql.expression.Expression;
import org.jetbrains.annotations.NotNull;

/** Represents a SQL {@code DELETE} statement that can be executed to delete rows from a table. */
public interface DeleteStatement extends Statement {
    /**
     * Adds a {@code WHERE} clause to the {@code DELETE} statement with the specified condition.
     *
     * @param condition The condition to be applied in the {@code WHERE} clause
     * @return A {@link DeleteStatement} with the added {@code WHERE} clause
     */
    @NotNull DeleteStatement where(@NotNull Expression condition);
}
