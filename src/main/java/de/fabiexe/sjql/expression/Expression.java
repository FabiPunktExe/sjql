package de.fabiexe.sjql.expression;

import de.fabiexe.sjql.column.Column;
import de.fabiexe.sjql.expression.constant.*;
import de.fabiexe.sjql.expression.dynamic.ColumnExpression;
import de.fabiexe.sjql.expression.dynamic.CurrentTimestampExpression;
import de.fabiexe.sjql.expression.logical.*;
import kotlin.uuid.UuidKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
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

    static @NotNull Expression constant(@Nullable String value) {
        if (value == null) {
            return new NullExpression();
        } else {
            return new StringExpression(value);
        }
    }

    static @NotNull Expression constant(@Nullable UUID value) {
        if (value == null) {
            return new NullExpression();
        } else {
            return new UUIDExpression(value);
        }
    }

    static @NotNull Expression constant(@Nullable kotlin.uuid.Uuid value) {
        if (value == null) {
            return new NullExpression();
        } else {
            return new UUIDExpression(UuidKt.toJavaUuid(value));
        }
    }

    static @NotNull Expression constant(@Nullable Instant value) {
        if (value == null) {
            return new NullExpression();
        } else {
            return new TimestampExpression(value);
        }
    }

    static @NotNull Expression constant(@Nullable kotlin.time.Instant value) {
        if (value == null) {
            return new NullExpression();
        } else {
            return new TimestampExpression(kotlin.time.jdk8.InstantConversionsJDK8Kt.toJavaInstant(value));
        }
    }

    static @NotNull Expression constant(@Nullable Object value) {
        if (value == null) {
            return new NullExpression();
        } else {
            return switch (value) {
                case Integer i -> new IntExpression(i);
                case Long l -> new LongExpression(l);
                case Float f -> new FloatExpression(f);
                case Double d -> new DoubleExpression(d);
                case Boolean b -> new BooleanExpression(b);
                case String s -> new StringExpression(s);
                case UUID u -> new UUIDExpression(u);
                case kotlin.uuid.Uuid u -> new UUIDExpression(UuidKt.toJavaUuid(u));
                case Instant t -> new TimestampExpression(t);
                case kotlin.time.Instant t -> new TimestampExpression(kotlin.time.jdk8.InstantConversionsJDK8Kt.toJavaInstant(t));
                default -> throw new IllegalArgumentException("Unsupported constant value type: " + value.getClass().getName());
            };
        }
    }

    static @NotNull Expression column(@NotNull Column<?> column) {
        return new ColumnExpression<>(column);
    }

    static @NotNull Expression currentTimestamp() {
        return new CurrentTimestampExpression();
    }

    default @NotNull Expression eq(@NotNull Expression other) {
        return new EqualsExpression(this, other);
    }

    default @NotNull Expression neq(@NotNull Expression other) {
        return new NotEqualsExpression(this, other);
    }

    default @NotNull Expression gt(@NotNull Expression other) {
        return new GreaterThanExpression(this, other);
    }

    default @NotNull Expression gte(@NotNull Expression other) {
        return new GreaterThanOrEqualExpression(this, other);
    }

    default @NotNull Expression lt(@NotNull Expression other) {
        return new LessThanExpression(this, other);
    }

    default @NotNull Expression lte(@NotNull Expression other) {
        return new LessThanOrEqualExpression(this, other);
    }

    default @NotNull Expression and(@NotNull Expression other) {
        return new AndExpression(this, other);
    }

    default @NotNull Expression or(@NotNull Expression other) {
        return new OrExpression(this, other);
    }

    default @NotNull Expression xor(@NotNull Expression other) {
        return new XorExpression(this, other);
    }

    default @NotNull Expression not() {
        return new NotExpression(this);
    }
}
