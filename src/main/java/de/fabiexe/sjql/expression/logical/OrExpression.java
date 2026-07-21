package de.fabiexe.sjql.expression.logical;

import de.fabiexe.sjql.expression.Expression;

/**
 * A logical {@code OR} expression.
 *
 * @param a the left-hand side
 * @param b the right-hand side
 */
public record OrExpression(Expression a, Expression b) implements LogicalExpression {}