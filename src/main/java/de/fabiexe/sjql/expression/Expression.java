package de.fabiexe.sjql.expression;

import de.fabiexe.sjql.expression.constant.*;
import de.fabiexe.sjql.expression.dynamic.CurrentTimestampExpression;
import de.fabiexe.sjql.expression.logical.*;
import kotlin.uuid.UuidKt;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents a SQL expression. Expressions can be combined with comparison, logical and arithmetic operators
 * to build {@code WHERE}, {@code ORDER BY} and {@code SET} clauses.
 * Use {@link #constant(Object)} to create an expression from a Java value and {@link Expression#currentTimestamp()}
 * for the database's current timestamp.
 */
public interface Expression {
    /**
     * Creates a constant expression from the given Java value.
     * Supported value types are {@code null}, {@link Integer}, {@link Long}, {@link Float},
     * {@link Double}, {@link Boolean}, {@link String}, {@link UUID}, {@link Instant} and their
     * Kotlin counterparts.
     *
     * @param value the value to convert
     * @return a {@link ConstantExpression} representing the value
     * @throws IllegalArgumentException if the value type is not supported
     */
    static Expression constant(@Nullable Object value) {
        return switch (value) {
            case null -> NullExpression.INSTANCE;
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

    /**
     * Creates an expression that evaluates to the database's current timestamp.
     *
     * @return a {@link CurrentTimestampExpression}
     */
    static Expression currentTimestamp() {
        return new CurrentTimestampExpression();
    }

    /**
     * Creates an {@code =} comparison with another expression.
     *
     * @param other the expression to compare with
     * @return a new {@link EqualsExpression}
     */
    default Expression eq(Expression other) {
        return new EqualsExpression(this, other);
    }

    /**
     * Creates an {@code =} comparison with a constant value.
     *
     * @param other the value to compare with, or {@code null}
     * @return a new {@link EqualsExpression}
     */
    default Expression eq(@Nullable Object other) {
        return eq(constant(other));
    }

    /**
     * Creates a {@code !=} comparison with another expression.
     *
     * @param other the expression to compare with
     * @return a new {@link NotEqualsExpression}
     */
    default Expression neq(Expression other) {
        return new NotEqualsExpression(this, other);
    }

    /**
     * Creates a {@code !=} comparison with a constant value.
     *
     * @param other the value to compare with, or {@code null}
     * @return a new {@link NotEqualsExpression}
     */
    default Expression neq(@Nullable Object other) {
        return neq(constant(other));
    }

    /**
     * Creates a {@code >} comparison with another expression.
     *
     * @param other the expression to compare with
     * @return a new {@link GreaterThanExpression}
     */
    default Expression gt(Expression other) {
        return new GreaterThanExpression(this, other);
    }

    /**
     * Creates a {@code >} comparison with a constant value.
     *
     * @param other the value to compare with, or {@code null}
     * @return a new {@link GreaterThanExpression}
     */
    default Expression gt(@Nullable Object other) {
        return gt(constant(other));
    }

    /**
     * Creates a {@code >=} comparison with another expression.
     *
     * @param other the expression to compare with
     * @return a new {@link GreaterThanOrEqualExpression}
     */
    default Expression gte(Expression other) {
        return new GreaterThanOrEqualExpression(this, other);
    }

    /**
     * Creates a {@code >=} comparison with a constant value.
     *
     * @param other the value to compare with, or {@code null}
     * @return a new {@link GreaterThanOrEqualExpression}
     */
    default Expression gte(@Nullable Object other) {
        return gte(constant(other));
    }

    /**
     * Creates a {@code <} comparison with another expression.
     *
     * @param other the expression to compare with
     * @return a new {@link LessThanExpression}
     */
    default Expression lt(Expression other) {
        return new LessThanExpression(this, other);
    }

    /**
     * Creates a {@code <} comparison with a constant value.
     *
     * @param other the value to compare with, or {@code null}
     * @return a new {@link LessThanExpression}
     */
    default Expression lt(@Nullable Object other) {
        return lt(constant(other));
    }

    /**
     * Creates a {@code <=} comparison with another expression.
     *
     * @param other the expression to compare with
     * @return a new {@link LessThanOrEqualExpression}
     */
    default Expression lte(Expression other) {
        return new LessThanOrEqualExpression(this, other);
    }

    /**
     * Creates a {@code <=} comparison with a constant value.
     *
     * @param other the value to compare with, or {@code null}
     * @return a new {@link LessThanOrEqualExpression}
     */
    default Expression lte(@Nullable Object other) {
        return lte(constant(other));
    }

    /**
     * Creates a logical {@code AND} with another expression.
     *
     * @param other the expression to combine with
     * @return a new {@link AndExpression}
     */
    default Expression and(Expression other) {
        return new AndExpression(this, other);
    }

    /**
     * Creates a logical {@code AND} with a constant value.
     *
     * @param other the value to combine with, or {@code null}
     * @return a new {@link AndExpression}
     */
    default Expression and(@Nullable Object other) {
        return and(constant(other));
    }

    /**
     * Creates a logical {@code OR} with another expression.
     *
     * @param other the expression to combine with
     * @return a new {@link OrExpression}
     */
    default Expression or(Expression other) {
        return new OrExpression(this, other);
    }

    /**
     * Creates a logical {@code OR} with a constant value.
     *
     * @param other the value to combine with, or {@code null}
     * @return a new {@link OrExpression}
     */
    default Expression or(@Nullable Object other) {
        return or(constant(other));
    }

    /**
     * Creates a logical {@code XOR} with another expression.
     *
     * @param other the expression to combine with
     * @return a new {@link XorExpression}
     */
    default Expression xor(Expression other) {
        return new XorExpression(this, other);
    }

    /**
     * Creates a logical {@code XOR} with a constant value.
     *
     * @param other the value to combine with, or {@code null}
     * @return a new {@link XorExpression}
     */
    default Expression xor(@Nullable Object other) {
        return xor(constant(other));
    }

    /**
     * Creates a logical {@code NOT} of this expression.
     *
     * @return a new {@link NotExpression}
     */
    default Expression not() {
        return new NotExpression(this);
    }

    /**
     * Creates an {@code IS NULL} check for this expression.
     *
     * @return a new {@link IsNullExpression}
     */
    default Expression checkNull() {
        return new IsNullExpression(this);
    }

    /**
     * Creates an {@code IS NOT NULL} check for this expression.
     *
     * @return a new {@link IsNotNullExpression}
     */
    default Expression checkNotNull() {
        return new IsNotNullExpression(this);
    }
}