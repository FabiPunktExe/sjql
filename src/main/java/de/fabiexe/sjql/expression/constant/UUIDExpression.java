package de.fabiexe.sjql.expression.constant;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record UUIDExpression(@NotNull UUID value) implements ConstantExpression<@NotNull UUID> {}
