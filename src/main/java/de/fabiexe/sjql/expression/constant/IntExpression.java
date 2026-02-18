package de.fabiexe.sjql.expression.constant;

import org.jetbrains.annotations.Nullable;

public record IntExpression(@Nullable Integer value) implements ConstantExpression<Integer> {}
