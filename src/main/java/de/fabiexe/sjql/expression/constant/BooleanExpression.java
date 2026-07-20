package de.fabiexe.sjql.expression.constant;

import org.jetbrains.annotations.NotNull;

/**
 * A constant boolean expression.
 *
 * @param value the boolean value
 */
public record BooleanExpression(@NotNull Boolean value) implements ConstantExpression<@NotNull Boolean> {}