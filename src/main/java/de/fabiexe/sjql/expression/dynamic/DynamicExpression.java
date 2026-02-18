package de.fabiexe.sjql.expression.dynamic;

import de.fabiexe.sjql.expression.Expression;

public sealed interface DynamicExpression extends Expression
        permits ColumnExpression, CurrentTimestampExpression {}
