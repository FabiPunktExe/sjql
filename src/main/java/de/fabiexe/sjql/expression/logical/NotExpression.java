package de.fabiexe.sjql.expression.logical;

import de.fabiexe.sjql.expression.Expression;
import org.jetbrains.annotations.NotNull;

/**
 * A logical {@code NOT} expression.
 *
 * @param expression the expression to negate
 */
public record NotExpression(@NotNull Expression expression) implements LogicalExpression {}