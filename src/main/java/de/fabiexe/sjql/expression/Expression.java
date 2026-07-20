package de.fabiexe.sjql.expression;

import de.fabiexe.sjql.expression.constant.*;
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

    static @NotNull Expression currentTimestamp() {
        return new CurrentTimestampExpression();
    }

    /**
     * Creates an {@code =} comparison with another expression.
     *
     * @param other the expression to compare with
     * @return a new {@link EqualsExpression}
     */
    default @NotNull Expression eq(@NotNull Expression other) {
        return new EqualsExpression(this, other);
    }

    /**
     * Creates an {@code =} comparison with a constant value.
     *
     * @param other the value to compare with, or {@code null}
     * @return a new {@link EqualsExpression}
     */
    default @NotNull Expression eq(@Nullable Object other) {
        return eq(constant(other));
    }

    /**
     * Creates a {@code !=} comparison with another expression.
     *
     * @param other the expression to compare with
     * @return a new {@link NotEqualsExpression}
     */
    default @NotNull Expression neq(@NotNull Expression other) {
        return new NotEqualsExpression(this, other);
    }

    /**
     * Creates a {@code !=} comparison with a constant value.
     *
     * @param other the value to compare with, or {@code null}
     * @return a new {@link NotEqualsExpression}
     */
    default @NotNull Expression neq(@Nullable Object other) {
        return neq(constant(other));
    }

    /**
     * Creates a {@code >} comparison with another expression.
     *
     * @param other the expression to compare with
     * @return a new {@link GreaterThanExpression}
     */
    default @NotNull Expression gt(@NotNull Expression other) {
        return new GreaterThanExpression(this, other);
    }

    /**
     * Creates a {@code >} comparison with a constant value.
     *
     * @param other the value to compare with, or {@code null}
     * @return a new {@link GreaterThanExpression}
     */
    default @NotNull Expression gt(@Nullable Object other) {
        return gt(constant(other));
    }

    /**
     * Creates a {@code >=} comparison with another expression.
     *
     * @param other the expression to compare with
     * @return a new {@link GreaterThanOrEqualExpression}
     */
    default @NotNull Expression gte(@NotNull Expression other) {
        return new GreaterThanOrEqualExpression(this, other);
    }

    /**
     * Creates a {@code >=} comparison with a constant value.
     *
     * @param other the value to compare with, or {@code null}
     * @return a new {@link GreaterThanOrEqualExpression}
     */
    default @NotNull Expression gte(@Nullable Object other) {
        return gte(constant(other));
    }

    /**
     * Creates a {@code <} comparison with another expression.
     *
     * @param other the expression to compare with
     * @return a new {@link LessThanExpression}
     */
    default @NotNull Expression lt(@NotNull Expression other) {
        return new LessThanExpression(this, other);
    }

    /**
     * Creates a {@code <} comparison with a constant value.
     *
     * @param other the value to compare with, or {@code null}
     * @return a new {@link LessThanExpression}
     */
    default @NotNull Expression lt(@Nullable Object other) {
        return lt(constant(other));
    }

    /**
     * Creates a {@code <=} comparison with another expression.
     *
     * @param other the expression to compare with
     * @return a new {@link LessThanOrEqualExpression}
     */
    default @NotNull Expression lte(@NotNull Expression other) {
        return new LessThanOrEqualExpression(this, other);
    }

    /**
     * Creates a {@code <=} comparison with a constant value.
     *
     * @param other the value to compare with, or {@code null}
     * @return a new {@link LessThanOrEqualExpression}
     */
    default @NotNull Expression lte(@Nullable Object other) {
        return lte(constant(other));
    }

    /**
     * Creates a logical {@code AND} with another expression.
     *
     * @param other the expression to combine with
     * @return a new {@link AndExpression}
     */
    default @NotNull Expression and(@NotNull Expression other) {
        return new AndExpression(this, other);
    }

    /**
     * Creates a logical {@code AND} with a constant value.
     *
     * @param other the value to combine with, or {@code null}
     * @return a new {@link AndExpression}
     */
    default @NotNull Expression and(@Nullable Object other) {
        return and(constant(other));
    }

    /**
     * Creates a logical {@code OR} with another expression.
     *
     * @param other the expression to combine with
     * @return a new {@link OrExpression}
     */
    default @NotNull Expression or(@NotNull Expression other) {
        return new OrExpression(this, other);
    }

    /**
     * Creates a logical {@code OR} with a constant value.
     *
     * @param other the value to combine with, or {@code null}
     * @return a new {@link OrExpression}
     */
    default @NotNull Expression or(@Nullable Object other) {
        return or(constant(other));
    }

    /**
     * Creates a logical {@code XOR} with another expression.
     *
     * @param other the expression to combine with
     * @return a new {@link XorExpression}
     */
    default @NotNull Expression xor(@NotNull Expression other) {
        return new XorExpression(this, other);
    }

    /**
     * Creates a logical {@code XOR} with a constant value.
     *
     * @param other the value to combine with, or {@code null}
     * @return a new {@link XorExpression}
     */
    default @NotNull Expression xor(@Nullable Object other) {
        return xor(constant(other));
    }

    /**
     * Creates a logical {@code NOT} of this expression.
     *
     * @return a new {@link NotExpression}
     */
    default @NotNull Expression not() {
        return new NotExpression(this);
    }

    /**
     * Creates an {@code IS NULL} check for this expression.
     *
     * @return a new {@link IsNullExpression}
     */
    default @NotNull Expression checkNull() {
        return new IsNullExpression(this);
    }

    /**
     * Creates an {@code IS NOT NULL} check for this expression.
     *
     * @return a new {@link IsNotNullExpression}
     */
    default @NotNull Expression checkNotNull() {
        return new IsNotNullExpression(this);
    }
}