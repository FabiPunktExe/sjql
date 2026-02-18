package de.fabiexe.sjql.expression.constant;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public record UUIDExpression(@Nullable UUID value) implements ConstantExpression<UUID> {}
