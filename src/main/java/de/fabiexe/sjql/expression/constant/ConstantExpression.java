package de.fabiexe.sjql.expression.constant;

import de.fabiexe.sjql.expression.Expression;

/**
 * An {@link Expression} that represents a constant value.
 *
 * @param <T> the type of the constant value
 */
public sealed interface ConstantExpression<T> extends Expression
        permits BooleanExpression, DoubleExpression, FloatExpression, IntExpression, LongExpression, StringExpression, UUIDExpression, TimestampExpression, NullExpression {
    /**
     * Gets the constant value.
     *
     * @return the value, or {@code null} for {@link NullExpression}
     */
    T value();
}