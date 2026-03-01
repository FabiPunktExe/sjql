package de.fabiexe.sjql.expression.constant;

import de.fabiexe.sjql.expression.Expression;
import org.jetbrains.annotations.Nullable;

public sealed interface ConstantExpression<T> extends Expression
        permits BooleanExpression, DoubleExpression, FloatExpression, IntExpression, LongExpression, StringExpression, UUIDExpression, TimestampExpression {
    @Nullable T value();
}
