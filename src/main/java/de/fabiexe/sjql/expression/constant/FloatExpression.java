package de.fabiexe.sjql.expression.constant;

import org.jetbrains.annotations.NotNull;

public record FloatExpression(@NotNull Float value) implements ConstantExpression<@NotNull Float> {}
