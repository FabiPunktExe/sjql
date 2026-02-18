package de.fabiexe.sjql.expression.constant;

import org.jetbrains.annotations.Nullable;

public record LongExpression(@Nullable Long value) implements ConstantExpression<Long> {}
