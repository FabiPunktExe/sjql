package de.fabiexe.sjql.expression.constant;

/**
 * A constant long expression.
 *
 * @param value the long value
 */
public record LongExpression(Long value) implements ConstantExpression<Long> {}