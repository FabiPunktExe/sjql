package de.fabiexe.sjql.expression.constant;

import org.jetbrains.annotations.NotNull;

/**
 * A constant double expression.
 *
 * @param value the double value
 */
public record DoubleExpression(@NotNull Double value) implements ConstantExpression<@NotNull Double> {}