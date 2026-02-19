package de.fabiexe.sjql.expression.logical;

import de.fabiexe.sjql.expression.Expression;

public sealed interface LogicalExpression extends Expression
        permits EqualsExpression, GreaterThanExpression, GreaterThanOrEqualExpression, LessThanExpression, LessThanOrEqualExpression, NotEqualsExpression {}
