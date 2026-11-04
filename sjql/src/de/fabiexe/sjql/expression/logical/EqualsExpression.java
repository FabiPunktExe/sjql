package de.fabiexe.sjql.expression.logical;

import de.fabiexe.sjql.expression.Expression;

/**
 * An equality ({@code =}) expression.
 *
 * @param a the left-hand side
 * @param b the right-hand side
 */
public record EqualsExpression(Expression a, Expression b) implements LogicalExpression {}