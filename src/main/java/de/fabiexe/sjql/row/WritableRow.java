package de.fabiexe.sjql.row;

import de.fabiexe.sjql.column.Column;
import de.fabiexe.sjql.expression.Expression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface WritableRow {
    <U> void set(@NotNull Column<U> column, @NotNull Expression value);
    <U> @Nullable Expression get(@NotNull Column<U> column);
    boolean contains(@NotNull Column<?> column);

    default <U> void set(@NotNull Column<U> column, @Nullable U value) {
        set(column, Expression.constant(value));
    }
}
