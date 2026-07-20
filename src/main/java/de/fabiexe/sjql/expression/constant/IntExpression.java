package de.fabiexe.sjql.expression.constant;

import org.jetbrains.annotations.NotNull;

/**
 * A constant integer expression.
 *
 * @param value the integer value
 */
public record IntExpression(@NotNull Integer value) implements ConstantExpression<@NotNull Integer> {}