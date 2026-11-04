package de.fabiexe.sjql.expression.logical;

import de.fabiexe.sjql.expression.Expression;

/**
 * An {@code IS NULL} check expression.
 *
 * @param expression the expression to check
 */
public record IsNullExpression(Expression expression) implements Expression {}