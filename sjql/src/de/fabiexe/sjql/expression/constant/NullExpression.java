package de.fabiexe.sjql.expression.constant;

import org.jspecify.annotations.Nullable;

/** A constant expression representing the SQL {@code NULL} value. */
public final class NullExpression implements ConstantExpression<@Nullable Object> {
    /** The singleton instance. */
    public static final NullExpression INSTANCE = new NullExpression();

    private NullExpression() {}

    @Override
    public @Nullable Object value() {
        return null;
    }
}