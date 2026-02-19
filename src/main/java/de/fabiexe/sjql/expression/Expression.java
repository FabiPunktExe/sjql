package de.fabiexe.sjql.expression;

import de.fabiexe.sjql.column.Column;
import de.fabiexe.sjql.expression.constant.*;
import de.fabiexe.sjql.expression.dynamic.ColumnExpression;
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

    static @NotNull Expression constant(long value) {
        return new LongExpression(value);
    }

    static @NotNull Expression constant(float value) {
        return new FloatExpression(value);
    }

    static @NotNull Expression constant(boolean value) {
        return new BooleanExpression(value);
    }

    static @NotNull Expression constant(@NotNull String value) {
        return new StringExpression(value);
    }

    static @NotNull Expression constant(@NotNull UUID value) {
        return new UUIDExpression(value);
    }

    static @NotNull Expression constant(@NotNull Object value) {
        return switch (value) {
            case Integer i -> new IntExpression(i);
            case Long l -> new LongExpression(l);
            case Float f -> new FloatExpression(f);
            case Double d -> new DoubleExpression(d);
            case Boolean b -> new BooleanExpression(b);
            case String s -> new StringExpression(s);
            case UUID u -> new UUIDExpression(u);
            default -> throw new IllegalArgumentException("Unsupported constant value type: " + value.getClass().getName());
        };
    }

    static @NotNull Expression column(@NotNull Column<?> column) {
        return new ColumnExpression<>(column);
    }

    static @NotNull Expression currentTimestamp() {
        return new CurrentTimestampExpression();
    }
}
