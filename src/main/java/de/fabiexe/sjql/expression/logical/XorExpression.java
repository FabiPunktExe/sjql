package de.fabiexe.sjql.expression.logical;

import de.fabiexe.sjql.expression.Expression;
import org.jetbrains.annotations.NotNull;

public record XorExpression(@NotNull Expression a, @NotNull Expression b) implements LogicalExpression {}
