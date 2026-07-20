package de.fabiexe.sjql.expression.logical;

import de.fabiexe.sjql.expression.Expression;
import org.jetbrains.annotations.NotNull;

/**
 * An {@code IS NULL} check expression.
 *
 * @param expression the expression to check
 */
public record IsNullExpression(@NotNull Expression expression) implements Expression {}