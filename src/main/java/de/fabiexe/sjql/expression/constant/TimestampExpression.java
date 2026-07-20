package de.fabiexe.sjql.expression.constant;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * A constant timestamp expression.
 *
 * @param value the timestamp value
 */
public record TimestampExpression(@NotNull Instant value) implements ConstantExpression<@NotNull Instant> {}