package de.fabiexe.sjql.expression.constant;

import de.fabiexe.sjql.expression.Expression;
import org.jetbrains.annotations.Nullable;

public sealed interface ConstantExpression<T> extends Expression
        permits DoubleExpression, IntExpression, StringExpression {
    @Nullable T value();
}
