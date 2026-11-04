package de.fabiexe.sjql.expression.logical;

import de.fabiexe.sjql.expression.Expression;

/**
 * A {@code >} comparison expression.
 *
 * @param a the left-hand side
 * @param b the right-hand side
 */
public record GreaterThanExpression(Expression a, Expression b) implements LogicalExpression {}