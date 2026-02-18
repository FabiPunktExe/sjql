package de.fabiexe.sjql.row;

import de.fabiexe.sjql.column.Column;
import de.fabiexe.sjql.expression.Expression;
import de.fabiexe.sjql.expression.constant.DoubleExpression;
import de.fabiexe.sjql.expression.constant.IntExpression;
import de.fabiexe.sjql.expression.constant.StringExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface WritableRow {
    <U> void set(@NotNull Column<U> column, @NotNull Expression value);
    <U> @Nullable Expression get(@NotNull Column<U> column);
    boolean contains(@NotNull Column<?> column);

    default <U> void set(@NotNull Column<U> column, @NotNull U value) {
        switch (value) {
            case Integer i -> set(column, new IntExpression(i));
            case Double d -> set(column, new DoubleExpression(d));
            case String s -> set(column, new StringExpression(s));
            default -> throw new IllegalArgumentException("Unsupported value type: " + value.getClass().getName());
        }
    }
}
