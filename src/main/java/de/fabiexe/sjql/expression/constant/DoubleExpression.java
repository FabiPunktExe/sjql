package de.fabiexe.sjql.expression.constant;

import org.jetbrains.annotations.Nullable;

public record DoubleExpression(@Nullable Double value) implements ConstantExpression<Double> {}
