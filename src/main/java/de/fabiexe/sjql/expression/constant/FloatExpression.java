package de.fabiexe.sjql.expression.constant;

import org.jetbrains.annotations.Nullable;

public record FloatExpression(@Nullable Float value) implements ConstantExpression<Float> {}
