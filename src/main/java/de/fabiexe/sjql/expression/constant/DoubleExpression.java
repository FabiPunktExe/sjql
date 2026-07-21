package de.fabiexe.sjql.expression.constant;

/**
 * A constant double expression.
 *
 * @param value the double value
 */
public record DoubleExpression(Double value) implements ConstantExpression<Double> {}