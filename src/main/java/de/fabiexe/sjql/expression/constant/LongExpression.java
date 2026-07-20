package de.fabiexe.sjql.expression.constant;

import org.jetbrains.annotations.NotNull;

/**
 * A constant long expression.
 *
 * @param value the long value
 */
public record LongExpression(@NotNull Long value) implements ConstantExpression<@NotNull Long> {}