package de.fabiexe.sjql.expression.constant;

import org.jetbrains.annotations.NotNull;

public record DoubleExpression(@NotNull Double value) implements ConstantExpression<@NotNull Double> {}
