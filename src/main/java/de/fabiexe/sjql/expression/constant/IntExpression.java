package de.fabiexe.sjql.expression.constant;

import org.jetbrains.annotations.NotNull;

public record IntExpression(@NotNull Integer value) implements ConstantExpression<@NotNull Integer> {}
