package de.fabiexe.sjql.expression.constant;

import org.jetbrains.annotations.NotNull;

/**
 * A constant string expression.
 *
 * @param value the string value
 */
public record StringExpression(@NotNull String value) implements ConstantExpression<@NotNull String> {}