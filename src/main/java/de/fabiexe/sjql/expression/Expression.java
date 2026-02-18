package de.fabiexe.sjql.expression;

import de.fabiexe.sjql.expression.constant.*;
import de.fabiexe.sjql.expression.dynamic.CurrentTimestampExpression;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface Expression {
    static @NotNull Expression constant(int value) {
        return new IntExpression(value);
    }

    static @NotNull Expression constant(double value) {
        return new DoubleExpression(value);
    }

    static @NotNull Expression constant(@NotNull String value) {
        return new StringExpression(value);
    }

    static @NotNull Expression constant(long value) {
        return new LongExpression(value);
    }

    static @NotNull Expression constant(float value) {
        return new FloatExpression(value);
    }

    static @NotNull Expression constant(boolean value) {
        return new BooleanExpression(value);
    }

    static @NotNull Expression constant(@NotNull UUID value) {
        return new UUIDExpression(value);
    }

    static @NotNull Expression currentTimestamp() {
        return new CurrentTimestampExpression();
    }
}
