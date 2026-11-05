package de.fabiexe.sjql.expression.dynamic;

import de.fabiexe.sjql.column.Column;
import de.fabiexe.sjql.column.ColumnRef;
import org.jspecify.annotations.Nullable;

/**
 * An expression that references a database column.
 *
 * @param <T> The type of the column
 * @param column The referenced column
 */
public record ColumnExpression<T extends @Nullable Object>(ColumnRef<T> column) implements DynamicExpression {
    public ColumnExpression(Column<T> column) {
        this(column.table().ref().column(column));
    }
}