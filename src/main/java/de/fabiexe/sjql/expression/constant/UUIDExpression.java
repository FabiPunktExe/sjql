package de.fabiexe.sjql.expression.constant;

import java.util.UUID;

/**
 * A constant UUID expression.
 *
 * @param value the UUID value
 */
public record UUIDExpression(UUID value) implements ConstantExpression<UUID> {}