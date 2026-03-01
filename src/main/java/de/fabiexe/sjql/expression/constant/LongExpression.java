package de.fabiexe.sjql.expression.constant;

import org.jetbrains.annotations.NotNull;

public record LongExpression(@NotNull Long value) implements ConstantExpression<@NotNull Long> {}
