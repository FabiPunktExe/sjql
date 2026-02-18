package de.fabiexe.sjql;

import de.fabiexe.sjql.expression.Expression;
import org.jetbrains.annotations.NotNull;

public interface DeleteStatement extends Statement {
    @NotNull DeleteStatement where(@NotNull Expression condition);
}
