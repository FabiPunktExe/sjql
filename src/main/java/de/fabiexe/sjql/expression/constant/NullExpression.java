package de.fabiexe.sjql.expression.constant;

import org.jetbrains.annotations.Nullable;

public final class NullExpression implements ConstantExpression<@Nullable Object> {
    @Override
    public @Nullable Object value() {
        return null;
    }
}
