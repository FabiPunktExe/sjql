package de.fabiexe.sjql.expression.dynamic;

import de.fabiexe.sjql.column.Column;
import org.jspecify.annotations.Nullable;

/**
 * An expression that references a database column.
 *
 * @param <T> the type of the column
 * @param column the referenced column
 */
public record ColumnExpression<T extends @Nullable Object>(Column<T> column) implements DynamicExpression {}