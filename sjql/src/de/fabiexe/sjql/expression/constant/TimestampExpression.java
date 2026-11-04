package de.fabiexe.sjql.expression.constant;

import java.time.Instant;

/**
 * A constant timestamp expression.
 *
 * @param value the timestamp value
 */
public record TimestampExpression(Instant value) implements ConstantExpression<Instant> {}