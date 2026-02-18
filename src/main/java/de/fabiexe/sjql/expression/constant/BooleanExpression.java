package de.fabiexe.sjql.expression.constant;

import org.jetbrains.annotations.Nullable;

public record BooleanExpression(@Nullable Boolean value) implements ConstantExpression<Boolean> {}
