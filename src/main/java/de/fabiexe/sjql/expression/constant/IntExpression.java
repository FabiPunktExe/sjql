package de.fabiexe.sjql.expression.constant;

/**
 * A constant integer expression.
 *
 * @param value the integer value
 */
public record IntExpression(Integer value) implements ConstantExpression<Integer> {}