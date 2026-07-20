package de.fabiexe.sjql.expression.dynamic;

/** An expression that evaluates to the database's current timestamp. */
public record CurrentTimestampExpression() implements DynamicExpression {}