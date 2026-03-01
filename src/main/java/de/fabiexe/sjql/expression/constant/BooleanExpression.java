package de.fabiexe.sjql.expression.constant;

import org.jetbrains.annotations.NotNull;

public record BooleanExpression(@NotNull Boolean value) implements ConstantExpression<@NotNull Boolean> {}
