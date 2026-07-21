package de.fabiexe.sjql.expression.logical;

import de.fabiexe.sjql.expression.Expression;

/**
 * A logical {@code NOT} expression.
 *
 * @param expression the expression to negate
 */
public record NotExpression(Expression expression) implements LogicalExpression {}