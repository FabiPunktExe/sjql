package de.fabiexe.sjql.row;

import de.fabiexe.sjql.column.Column;
import de.fabiexe.sjql.expression.Expression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface WritableRow {
    <U> void set(@NotNull Column<U> column, @NotNull Expression value);
    <U> @Nullable Expression get(@NotNull Column<U> column);
    boolean contains(@NotNull Column<?> column);

    default <U> void set(@NotNull Column<U> column, @NotNull U value) {
        set(column, switch (value) {
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
