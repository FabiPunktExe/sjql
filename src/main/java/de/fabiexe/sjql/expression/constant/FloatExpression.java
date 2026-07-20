package de.fabiexe.sjql.expression.constant;

import org.jetbrains.annotations.NotNull;

/**
 * A constant float expression.
 *
 * @param value the float value
 */
public record FloatExpression(@NotNull Float value) implements ConstantExpression<@NotNull Float> {}