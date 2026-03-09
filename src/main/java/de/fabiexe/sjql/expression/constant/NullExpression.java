package de.fabiexe.sjql.expression.constant;

import org.jetbrains.annotations.Nullable;

public final class NullExpression implements ConstantExpression<@Nullable Object> {
    public static final NullExpression INSTANCE = new NullExpression();

    private NullExpression() {}

    @Override
    public @Nullable Object value() {
        return null;
    }
}
