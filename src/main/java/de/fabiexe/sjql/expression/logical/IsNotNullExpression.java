package de.fabiexe.sjql.expression.logical;

import de.fabiexe.sjql.expression.Expression;
import org.jetbrains.annotations.NotNull;

public record IsNotNullExpression(@NotNull Expression expression) implements Expression {}
