package de.fabiexe.sjql;

import de.fabiexe.sjql.expression.Expression;
import org.jetbrains.annotations.NotNull;

public interface UpdateStatement extends Statement {
    @NotNull UpdateStatement where(@NotNull Expression condition);
}
