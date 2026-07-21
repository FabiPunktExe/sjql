package de.fabiexe.sjql.expression.logical;

import de.fabiexe.sjql.expression.Expression;

/**
 * A logical {@code XOR} expression.
 *
 * @param a the left-hand side
 * @param b the right-hand side
 */
public record XorExpression(Expression a, Expression b) implements LogicalExpression {}