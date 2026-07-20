package de.fabiexe.sjql.expression.constant;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * A constant UUID expression.
 *
 * @param value the UUID value
 */
public record UUIDExpression(@NotNull UUID value) implements ConstantExpression<@NotNull UUID> {}