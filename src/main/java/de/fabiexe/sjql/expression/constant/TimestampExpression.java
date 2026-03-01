package de.fabiexe.sjql.expression.constant;

import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public record TimestampExpression(@Nullable Instant value) implements ConstantExpression<Instant> {}
