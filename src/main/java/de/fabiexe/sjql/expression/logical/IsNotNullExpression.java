package de.fabiexe.sjql.expression.logical;

import de.fabiexe.sjql.expression.Expression;

/**
 * An {@code IS NOT NULL} check expression.
 *
 * @param expression the expression to check
 */
public record IsNotNullExpression(Expression expression) implements Expression {}