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
    static @NotNull Expression constant(@Nullable Object value) {
        if (value == null) {
            return NullExpression.INSTANCE;
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

    default @NotNull Expression isNull() {
        return new IsNullExpression(this);
    }

    default @NotNull Expression isNotNull() {
        return new IsNotNullExpression(this);
    }
}
