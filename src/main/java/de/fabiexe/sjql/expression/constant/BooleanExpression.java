package de.fabiexe.sjql.expression.constant;

/**
 * A constant boolean expression.
 *
 * @param value the boolean value
 */
public record BooleanExpression(Boolean value) implements ConstantExpression<Boolean> {}