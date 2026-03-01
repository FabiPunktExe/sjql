package de.fabiexe.sjql.expression.constant;

import org.jetbrains.annotations.NotNull;

public record StringExpression(@NotNull String value) implements ConstantExpression<@NotNull String> {}
