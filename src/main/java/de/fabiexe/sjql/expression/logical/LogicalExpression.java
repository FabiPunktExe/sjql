package de.fabiexe.sjql.expression.logical;

import de.fabiexe.sjql.expression.Expression;

/**
 * A logical or comparison {@link Expression}.
 */
public sealed interface LogicalExpression extends Expression
        permits EqualsExpression, GreaterThanExpression, GreaterThanOrEqualExpression, LessThanExpression, LessThanOrEqualExpression, NotEqualsExpression, AndExpression, OrExpression, XorExpression, NotExpression {}