package de.fabiexe.sjql.expression.constant;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public record TimestampExpression(@NotNull Instant value) implements ConstantExpression<@NotNull Instant> {}
