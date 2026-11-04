package de.fabiexe.sjql.expression.dynamic;

import de.fabiexe.sjql.expression.Expression;

/** An {@link Expression} whose value is determined by the database at execution time. */
public sealed interface DynamicExpression extends Expression
        permits ColumnExpression, CurrentTimestampExpression {}