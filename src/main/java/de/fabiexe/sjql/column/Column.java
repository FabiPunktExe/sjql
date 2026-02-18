package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import de.fabiexe.sjql.expression.Expression;
import de.fabiexe.sjql.expression.dynamic.ColumnExpression;
import de.fabiexe.sjql.expression.logical.EqualsExpression;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public sealed interface Column<T> permits BasicColumn {
    @NotNull Table<?> table();
    @NotNull String name();
    @NotNull Class<T> type();
    @Nullable Expression defaultValue();
    @Contract("_ -> this")
    @NotNull Column<T> defaultValue(@NotNull Expression defaultValue);

    @Contract("_ -> this")
    default @NotNull Column<T> defaultValue(@NotNull T defaultValue) {
        return defaultValue(switch (defaultValue) {
            case Integer i -> Expression.constant(i);
            case Double d -> Expression.constant(d);
            case String s -> Expression.constant(s);
            case Long l -> Expression.constant(l);
            case Float f -> Expression.constant(f);
            case Boolean b -> Expression.constant(b);
            case UUID u -> Expression.constant(u);
            default -> throw new IllegalArgumentException("Unsupported value type: " + defaultValue.getClass().getName());
        });
    }

    default @NotNull Expression eq(@NotNull Expression value) {
        return new EqualsExpression(new ColumnExpression<>(this), value);
    }

    default @NotNull Expression eq(@NotNull Column<? extends T> other) {
        return eq(new ColumnExpression<>(other));
    }

    default @NotNull Expression eq(@NotNull T value) {
        return eq(switch (value) {
            case Integer i -> Expression.constant(i);
            case Double d -> Expression.constant(d);
            case String s -> Expression.constant(s);
            case Long l -> Expression.constant(l);
            case Float f -> Expression.constant(f);
            case Boolean b -> Expression.constant(b);
            case UUID u -> Expression.constant(u);
            default -> throw new IllegalArgumentException("Unsupported value type: " + value.getClass().getName());
        });
    }
}
