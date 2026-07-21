package de.fabiexe.sjql.expression.constant;

/**
 * A constant float expression.
 *
 * @param value the float value
 */
public record FloatExpression(Float value) implements ConstantExpression<Float> {}